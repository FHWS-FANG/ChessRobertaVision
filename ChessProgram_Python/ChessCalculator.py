import numpy as np
import chess

class ChessCalculator():

    #function to convert a boardConfig into a numpy array 
    def boardConfig_to_matrix(self, boardConfig):
        
        #initialize a 8x8-matrix containing only zeroes
        boardConfig_matrix = np.zeros((8,8), dtype = np.str_)
        #loop over all rows
        for i in range(8):
            row = boardConfig.split("/")[i] 
            
            #counter to save on which field we are at
            counter = 0
            #loop over all fields in a row
            for j in range(len(row)):
                #e.g. 4P3 gets converted to 0,0,0,0,P,0,0,0 and stored in the matrix
                if row[j].isnumeric():
                    counter += int(row[j])
                else:
                    boardConfig_matrix[i,counter] = row[j]
                    counter += 1
    
        return(boardConfig_matrix)
    
    
    #function to get the differences in two boardConfig_matrices (check field by field what has changed on that field)
    def fens_to_difference_list(self, boardConfig_matrix_start, boardConfig_matrix_new):
        #list to save all differences in the matrices
        differences = []
        #loop over all entries in the matrices
        for i in range(8):
            for j in range(8):
                #check if the entries are different
                if boardConfig_matrix_start[i,j] != boardConfig_matrix_new[i,j]:
                    #convert the indices to a chess field (e.g. (0,0)=a8, (4,4)=e2, (2,6)=g6)
                    field = chr(ord(str(j))+49) + str((8-i))
                    #add the field and the entries in the list
                    differences.append([field, boardConfig_matrix_start[i,j], boardConfig_matrix_new[i,j]])
              
        return(differences)
        
    #function that converts differences-list in a move, e.g. [['g8', 'n', 'Q'], ['f7', 'P', '']] == f7g8q
    #and generates a move_list for the robot (e.g. f7g8q has following move_list: ['f7', '', 'g8', '', 'Q', 'g8'] (empty fields means out!))
    def difference_list_to_move(self, differences, FEN_start):
        allowed = True
        move = ''
        move_list = []
        rnbq_list_small = ['r','n','b','q']
        rnbq_list_big = ['R','N','B','Q']
        #big case distinction: every legal move changes either 2, 3 or 4 fields
        if len(differences) == 2:
            #normal move (e.g. e2e4)
            if differences[0][1] == differences[1][2] and differences[0][2] == differences[1][1]:
                if differences[0][1] == '':
                    move = differences[1][0] + differences[0][0]
                    move_list.append(differences[1][0])
                    move_list.append(differences[0][0])
                else:
                    move = differences[0][0] + differences[1][0]
                    move_list.append(differences[0][0])
                    move_list.append(differences[1][0])
                    
            #normal capture
            if (differences[0][1] == differences[1][2]) and (differences[0][2] == '' and differences[1][1] != ''):
                move = differences[0][0] + differences[1][0]
                move_list.append(differences[1][0])
                move_list.append('')
                move_list.append(differences[0][0])
                move_list.append(differences[1][0])
            elif (differences[0][2] == differences[1][1]) and (differences[1][2] == '' and differences[0][1] != ''):
                move = differences[1][0] + differences[0][0]
                move_list.append(differences[0][0])
                move_list.append('')
                move_list.append(differences[1][0])
                move_list.append(differences[0][0])
                
            #normal promotion
            if differences[0][1] == 'p' and differences[1][2] in rnbq_list_small:
                if differences[0][2] == differences[1][1] == '':
                    move = differences[0][0] + differences[1][0] + differences[1][2]
                    move_list.append(differences[0][0])
                    move_list.append('')
                    move_list.append(differences[1][2])
                    move_list.append(differences[1][0])
            elif differences[1][1] == 'P' and differences[0][2] in rnbq_list_big:
                if differences[0][1] == differences[1][2] == '':
                    move = differences[1][0] + differences[0][0] + differences[0][2]
                    move_list.append(differences[1][0])
                    move_list.append('')
                    move_list.append(differences[0][2])
                    move_list.append(differences[0][0])
                    
            #capture promotion
            if differences[1][1] == 'P' and differences[1][2] == '' and differences[0][1] in rnbq_list_small and differences[0][2] in rnbq_list_big:
                move = differences[1][0] + differences[0][0] + differences[0][2]
                move_list.append(differences[0][0])
                move_list.append('')
                move_list.append(differences[1][0])
                move_list.append('')
                move_list.append(differences[0][2])
                move_list.append(differences[0][0])
            elif differences[0][1] == 'p' and differences[0][2] == '' and differences[1][1] in rnbq_list_big and differences[1][2] in rnbq_list_small:
                move = differences[0][0] + differences[1][0] + differences[1][2]
                move_list.append(differences[1][0])
                move_list.append('')
                move_list.append(differences[0][0])
                move_list.append('')
                move_list.append(differences[1][2])
                move_list.append(differences[1][0])
                
            #make the promotion letter small for notation
            if len(move) == 5 and ord(move[4]) < 97:
                move = move[0:4] + chr(ord(move[4])+32)
        
        #only an en passant move changes 3 fields    
        elif len(differences) == 3:
            #check if an en passant move is possible according to the FEN
            if FEN_start.split(" ")[3] != '-':
                indices_without_en_passant_field = []
                for i in range(3):
                    if differences[i][2] != '':
                        str2 = differences[i][0]
                        piece = differences[i][2]
                    else:
                        indices_without_en_passant_field.append(i)
                
                if piece == differences[indices_without_en_passant_field[0]][1]:
                    str1 = differences[indices_without_en_passant_field[0]][0]
                    str3 = differences[indices_without_en_passant_field[1]][0]
                elif piece == differences[indices_without_en_passant_field[1]][1]:
                    str1 = differences[indices_without_en_passant_field[1]][0]
                    str3 = differences[indices_without_en_passant_field[0]][0]
                        
                move = str1 + str2
                move_list.append(str3)
                move_list.append('')
                move_list.append(str1)
                move_list.append(str2)
            else:
                allowed = False
        
        #only castling changes 4 fields   
        elif len(differences) == 4:
            #check if  castling is allowed according to the FEN
            castle_rights = FEN_start.split(" ")[2]
            #kingside castling for white
            if 'K' in castle_rights and differences == [['e1', 'K', ''], ['f1', '', 'R'], ['g1', '', 'K'], ['h1', 'R', '']]:
                move = 'e1g1'
                str1 = 'h1'
                str2 = 'f1'
            #queenside castling for white
            elif 'Q' in castle_rights and differences == [['a1', 'R', ''], ['c1', '', 'K'], ['d1', '', 'R'], ['e1', 'K', '']]:
                move = 'e1c1'
                str1 = 'a1'
                str2 = 'd1'
            #kingside castling for black    
            elif 'k' in castle_rights and differences == [['e8', 'k', ''], ['f8', '', 'r'], ['g8', '', 'k'], ['h8', 'r', '']]:
                move = 'e8g8'
                str1 = 'h8'
                str2 = 'f8'
            #queenside castling for black
            elif 'q' in castle_rights and differences == [['a8', 'r', ''], ['c8', '', 'k'], ['d8', '', 'r'], ['e8', 'k', '']]:
                move = 'e8c8'
                str1 = 'a8'
                str2 = 'd8'
            else:
                allowed = False
        
            move_list.append(move[0:2])
            move_list.append(move[2:4])
            move_list.append(str1)
            move_list.append(str2)
        else:
            allowed = False
        
        return(move, move_list, allowed)
    
    
    
    #function, that calculates the move between a FEN (last position in fen format) and a boardConfig (current postion in boardConfig format)
    def move_calculator(self, FEN_start, boardConfig_new):
        #convert FEN to boardConfig by removing the extra parameter
        boardConfig_start = FEN_start.split(" ")[0]
        
        #convert the boardConfig to numpy arrays
        boardConfig_matrix_start = self.boardConfig_to_matrix(boardConfig_start)
        boardConfig_matrix_new = self.boardConfig_to_matrix(boardConfig_new)
        
        #get the differences in the two matrices and store them in a list
        differences = self.fens_to_difference_list(boardConfig_matrix_start, boardConfig_matrix_new)
        
        #convert the differences to a move and generate a move list for the robot. Also get information about castling
        [move, move_list, allowed] = self.difference_list_to_move(differences, FEN_start)
        
        #clear the move and move_list if the move was not possible by the amount of pieces rearanged, ... (moves like e2e5 are still possible for the moment and they will be treated in the main program)
        if(not allowed):
            move = ""
            move_list = []
        
        return(move, move_list)
        
        
    #function to check if the calculated move is legal in the position
    def is_move_legal(self, FEN_start, move):
        #make a board out of FEN_start
        board_start = chess.Board(FEN_start)
        
        #get all legal moves in FEN_start
        legalMoves = board_start.legal_moves
        
        #create a bool to tell if the move is legal
        legal = True
        
        #a try/except block is needed because the function to convert the move in uci format needs to get a legal move
        try:
            #convert move (string) to a uci format
            uci_move = chess.Move.from_uci(move)
        
            #check if the calculated move is legal in the starting position
            if uci_move not in legalMoves:
                legal = False   
        except:
            legal = False
        
        return(legal)
        
   
    #function that creates a move_list out of a move (e.g. e2e4 = ['e2', 'e4'], e4d5 = ['d5', '', 'e4', 'd5'])
    def move_to_move_list(self, FEN_start, move):
        #create a new board with FEN_start
        board_new = chess.Board(FEN_start)
        #convert move (string) to a uci format
        uci_move = chess.Move.from_uci(move)
        #execute the move on the new board
        board_new.push(uci_move)
        #now get the FEN of the board where the move was executed
        FEN_new = board_new.fen()
        #make the FEN_new to a boardConfig
        boardConfig_new = FEN_new.split(' ')[0]
        
        #use the function move_calculator to get the move_list
        [_, move_list] = self.move_calculator(FEN_start, boardConfig_new)
        
        return(move_list)
        
        
    
    #function that checks if there are kings in the boardConfig (criteria to resign the game)
    def kings_in_boardConfig(self, boardConfig):
        kings = True
        if ("K" not in boardConfig and "k" not in boardConfig):
            kings = False
        
        return(kings)
        
        
        
        
        
        
        
        