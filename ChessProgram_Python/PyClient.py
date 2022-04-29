from ClientClass import Client
from ChessCalculator import ChessCalculator
from stockfish import Stockfish
import chess
import math

class ProgrammClient():
    
    # get the whole program started
    def __init__(self):
        self.start() 

    def start(self):
        print("Hello, the chess program is starting. Please wait a moment...")

        #start the client and set ip and port
        myClient = Client()
        #myClient.setHost("localhost")
        myClient.setHost("192.168.70.12")
        myClient.setPort(30003)
        myClient.start()
        
        #Initialize the chess_calulator
        myChessCalculator = ChessCalculator()
        
        #initialize stockfish client
        stockfish = Stockfish(r"C:\Users\timse\Documents\FH\Master\Masterarbeit\python Chess\stockfish\stockfish-11-win\Windows\stockfish_20011801_x64.exe")
        
        #Initialize the chess starting position 
        board = chess.Board()
        
        #Bool variable to save if the player resigned
        resigned = False
        
        #try to receive a message from halcon to test if everything was found properly and the chess starting position was detected
        rec = False
        while(not rec):
            if(myClient.hasReceivedString()):
                recString = myClient.getReceivedString()
                ###print("Python received: " + recString)
            
            if(recString[0:43] == "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR"):
                rec = True
            elif(recString[0:2] == '1_'):
                print("There have been " + recString[2] + " '1's found on the chessboard. It has to be either 1 or 2. Please try again!")
            elif(recString[0:2] == 'A_'):
                print("There have been " + recString[2] + " 'A's found on the chessboard. It has to be either 1 or 2. Please try again!")
            elif(recString[0:2] == 'H_'):
                print("There have been " + recString[2] + " 'H's found on the chessboard. It has to be either 1 or 2. Please try again!")
            elif(recString == 'not_enough_pieces'):
                print("Halcon found not enough pieces on the chessboard. Please try again!")
            elif(recString == 'too_many_pieces'):
                print("Halcon found too many pieces on the chessboard. Please try again!")
            elif(recString == 'corners_not_found_correctly'):
                print("The corner stars of the chessboard are not found correctly. Please try again!")
            elif(recString == 'distance_A_H_is_out_of_range'):
                print("The distance between A and H is out of range. Please try again!")
            else:
                print("The chess starting position could not be detected. Please try again!")

        
        #get the angle of the chessboard from the recString to let the robot know what "parallel to the chessboard" is
        Angle_chessboard = recString.split('$')[1]
        
        #ask user which difficulty level stockfish should have
        print("Type the elo of the robot. It should be between 0 and 3000!")
        stockfish_elo_string = input()
        running = True
        while(running):
            try:
                stockfish_elo = float(stockfish_elo_string)
                if(stockfish_elo < 0 and stockfish_elo > 3000):
                    print("False input. Type a float between 0 and 3000!")
                    stockfish_elo = float(input())
                else:
                    running = False
            except:
                print("False input. Type a float between 0 and 3000!")
                stockfish_elo_string = input()
        
           
        #Ask user if the robot should be able to resign in a lost position
        print("Should the robot be able to resign in a lost position? Type 'y' for yes or 'n' for no!")
        stockfish_resign_input = input()
        running = True
        while(running):
            if(stockfish_resign_input == "y" or stockfish_resign_input == "n"):
                running = False
            else:
                print("False input! Type 'y' or 'n'!")
                stockfish_resign_input = input()
          
        #convert the input to a bool
        if(stockfish_resign_input == "y"):
            stockfish_resign_bool = True       
        elif(stockfish_resign_input == "n"):
            stockfish_resign_bool = False    
            
        #Ask user which color he/she wants to play
        print("Type 'w' to play with the white pieces or 'b' to play with the black pieces!")
        color = input()
        while((color != "w") and (color != "b")):
            print("False input. Type 'w' or 'b' to choose your color!")
            color = input()
        
        if(color == "w"):
            print("You are playing the white pieces! Make your first move!")
        else:
            print("You are playing the black pieces! Wait for the robot to make a move!")
        
        #rotate the Angle_chessboard 180degrees if the player plays the black pieces
        if(color == "b"):
            Angle_chessboard = str(float(Angle_chessboard) - math.pi)
        
        #variable if the whole program is running
        progRunning = True
        
        while(progRunning):
            #case distinction who's turn it is, either the user or the robot
            if(board.fen().split(" ")[1] == color):
                #USER:
                #Ask Halcon for a new BoardConfig by sending Halcon the statement "boardConfig"
                myClient.setSendString("boardConfig@cas")
                #waiting to receive the new BoardConfig
                rec = False
                while(not rec):
                    if(myClient.hasReceivedString()):
                        recString = myClient.getReceivedString()
                        ###print("Python received: " + recString)
                    
                    #check if there are both kings on the board, if not the player resigned and the game is over
                    are_kings_in_boardConfig = myChessCalculator.kings_in_boardConfig(recString)
                    if(not are_kings_in_boardConfig):
                        progRunning = False
                        resigned = True
                        break
                    
                    #get the move between the two positions
                    try:
                        [move, _] = myChessCalculator.move_calculator(board.fen(), recString)   
                    except:
                        move = ''
                        
                    #check if the move is legal in the current position
                    legal = myChessCalculator.is_move_legal(board.fen(), move)
                    
                    #if the move was legal, execute the move on the board and head over to the robots move
                    #if the move is illegal, state the detected move in the command window and wait for a new one
                    if(legal):
                        ###print("The following move was detected and executed: " + move)
                        #convert move (string) to a uci format
                        uci_move = chess.Move.from_uci(move)
                        #execute the move on the board
                        board.push(uci_move)
                        rec = True
                        
                        #check if the game is over 
                        if(board.is_game_over()):
                            progRunning = False
                            
                    else:
                        #give only an error message if it is not the same FEN (nothing changed)
                        if(board.fen() != recString and move != ''):
                            print("The following move was detected and is not legal: " + move)
                            
                        #Ask Halcon for a new BoardConfig by sending Halcon the statement "boardConfig"
                        myClient.setSendString("boardConfig@cas")
                        
            else:
                #ROBOT:
                #give stockfish the current FEN
                stockfish.set_fen_position(board.fen())
                
                #ask if stockfish wants to resign
                stockfish_resign = False
                if(stockfish_resign_bool):
                    stockfish_eval_type = stockfish.get_evaluation()["type"]
                    stockfish_eval_value = stockfish.get_evaluation()["value"]
                    if(stockfish_eval_type == "mate"):
                        if(color == "w" and stockfish_eval_value >= 1 and stockfish_eval_value <= 3):
                            stockfish_resign = True
                        elif(color == "b" and stockfish_eval_value <= -1 and stockfish_eval_value >= -3):
                            stockfish_resign = True
                    else:
                        if(color == "w" and stockfish_eval_value >= 1000):
                            stockfish_resign = True
                        elif(color == "b" and stockfish_eval_value <= -1000):
                            stockfish_resign = True
                
                
                if(stockfish_resign):
                    progRunning = False
                else:
                    #calculate stockfishs move (if the elo is set to 0, make the engine worse with setting the time to 1ms (mainly for debugging reasons))
                    if(stockfish_elo == 0):
                        stockfish.set_elo_rating(stockfish_elo)
                        move = stockfish.get_best_move_time(1)
                    else:
                        stockfish.set_elo_rating(stockfish_elo)
                        move = stockfish.get_best_move()
                    
                    print("Robots move = " + move)
                    
                    #in case the user should decide on robots moves, comment out the following line
                    #move = input()
                    
                    #calculate a movelist out of move (e.g. e2e4 -> ['e2', 'e4'] / g7h8q -> ['g7', '', 'h8', '', 'Q', 'h8'] / ...)
                    move_list = myChessCalculator.move_to_move_list(board.fen(), move)
                    
                    #get pose of all pieces for the even elements in movelist (including promotion pieces outside) 
                    pose_list_even = []
                    for i in range(0, len(move_list), 2):
                        field = move_list[i]
                        
                        rec = False
                        while(not rec):
                            sendString = 'get_pose_' + field + '@cas'
                            myClient.setSendString(sendString)
                            
                            if(myClient.hasReceivedString()):
                                recString = myClient.getReceivedString()
                                ###print("Python received: " + recString)
                            pose_list_even.append(recString)
                            rec = True
                    
                    #get pose of every empty field for the odd elements in movelist 
                    pose_list_odd = []
                    for i in range(1, len(move_list), 2):
                        field = move_list[i]
                        
                        #replace empty string with "out" + color, so halcon knows it has to calculate the pose of the place where the captured pieces will go to
                        if(field == ''):
                            field = 'out_' + color
                        
                        rec = False
                        while(not rec):
                            sendString = 'get_empty_field_pose_' + field + '@cas'
                            myClient.setSendString(sendString)
                            
                            if(myClient.hasReceivedString()):
                                recString = myClient.getReceivedString()
                                ###print("Python received: " + recString)
                            pose_list_odd.append(recString)
                            rec = True
                        
                     
                    #convert the lists to strings
                    pose_list_even_string = ''
                    for i in range(len(pose_list_even)):
                        pose_list_even_string = pose_list_even_string + pose_list_even[i]
                        #add a underscore between the elements
                        if(i != len(pose_list_even) - 1):
                            pose_list_even_string = pose_list_even_string + '_'
                        
                    pose_list_odd_string = ''
                    for i in range(len(pose_list_odd)):
                        pose_list_odd_string = pose_list_odd_string + pose_list_odd[i]
                        #add a underscore between the elements
                        if(i != len(pose_list_odd) - 1):
                            pose_list_odd_string = pose_list_odd_string + '_'
                        
                        
                    #combine the strings and seperate them with a $ sign (also add the Angle mean here)
                    pose_list_string = pose_list_even_string + '$' + pose_list_odd_string + '$' + Angle_chessboard
                    #send string to the robot 
                    sendString = pose_list_string + '@rob'
                    myClient.setSendString(sendString)
                    
                    #wait for the robot to send 'done' after the robot executed the move
                    rec = False
                    while(not rec):
                        if(myClient.hasReceivedString()):
                            recString = myClient.getReceivedString()
                            ###print("Python received: " + recString)
                        if(recString == 'done'):
                            rec = True
                    
                    #now update the board with the move executed by the robot
                    uci_move = chess.Move.from_uci(move)
                    board.push(uci_move)
                    
                    #check if the game is over
                    if(board.is_game_over()):
                        progRunning = False

         
        #shut down the connection
        sendString = 'kill'
        myClient.setSendString(sendString)
        
        #state who the winner is and why
        if(resigned):
            print("The robot wins, because of resignation by the player!")
        if(stockfish_resign):
            print("The user wins, because of resignation by the robot!")
        if(board.is_checkmate()):
            if(board.turn):
                print("Black won, because of checkmate!")
            else:
                print("White won, because of checkmate!")
        if(board.is_stalemate()):
            if(board.turn):
                print("Black won, because of stalemate!")
            else:
                print("White won, because of stalemate!")
        if(board.is_insufficient_material()):
            print("The game ends in a draw, because of insufficient material!")
        if(board.is_seventyfive_moves()):
            print("The game ends in a draw, because of the 75-move rule!")
        if(board.is_fivefold_repetition()):
            print("The game ends in a draw, because of fivefold repetition!")
        
        
        print("The chess programm will be closed. Goodbye!")    
        myClient.stop


#start everything 
pC = ProgrammClient()
