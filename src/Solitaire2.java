
import java.util.*;
/* COMP 2230 Data Structures and Algorithm Analysis
 * Solitaire Game Board
 * February 15 2020
 */

/**
 *
 * @author Adan David Sierra Calderon
 * T00555274
 */

public class Solitaire2  {
    
    private enum INDEXES { T0,T1,T2,T3,T4,T5,T6,FH,FD,FC,FS,stock,waste };
    
    private static Stack[] stacks = new Stack[13];
    private static ArrayDeque[] queues = new ArrayDeque[7];
    private static Scanner scan = new Scanner(System.in);
    static int input;
    static int T0 =0;
    static int T1 =1;
    static int T2 =2;
    static int T3 =3;
    static int T4 =4;
    static int T5 =5;
    static int T6 =6;
    static int FH =7;
    static int FD =8;
    static int FC =9;
    static int FS =10;
    static int stock =11;
    static int waste =12;
    
    public static void main(String[] args) throws InputMismatchException {
        
        //for(STACKS s: EnumSet.range(STACKS.T0,STACKS.T1) ) System.out.println( s.ordinal() );
        
        //System.exit(0);
        
        for(INDEXES i: INDEXES.values() )
            stacks[ i.ordinal() ] = new Stack<>();
        

        for(INDEXES i: EnumSet.range(INDEXES.T0,INDEXES.T6) )
            queues[ i.ordinal() ] = new ArrayDeque<>();


        Start();

        while (input != 6) {
            
            System.out.println("Stock contains: " + stacks[INDEXES.stock.ordinal()].size() + " cards");
            System.out.println("Top card of waste is showing " + (stacks[INDEXES.waste.ordinal()].empty() ? "(empty)" : stacks[INDEXES.waste.ordinal()].peek()) );
            
            System.out.println("<TABLEAUS>");                
            EnumSet.range(INDEXES.T0, INDEXES.T6).forEach( (i)-> {
                    System.out.println(i + " has " + queues[i.ordinal()].size() + " face down cards and " + stacks[i.ordinal()].size() + " face up cards, showing " + (stacks[i.ordinal()].empty() ? "(empty)" :  stacks[i.ordinal()].peek()) );
            });
                
            System.out.println();
            System.out.println("<FOUNDATIONS>");
            EnumSet.range(INDEXES.FH, INDEXES.FS).forEach( (i)-> {
                System.out.println(stacks[i.ordinal()].peek());
            });

            System.out.println();
            System.out.println("MENU \nEnter the NUMBER of the desired instruction");
            System.out.println("1.Draw a card from the Deck");
            System.out.println("2.Move waste card to foundation");
            System.out.println("3.Move waste card to tableau");
            System.out.println("4.Move tableau card to another tableau ");
            System.out.println("5.Move tableau card to foundations");
            System.out.println("6.Quit game");

            try {
                input = scan.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("**************************");//INPUT MISMATCH EXCEPTION IN CASE IT ENTERS A STRING TO ADD ROBUSTNESS
                System.out.println("INTEGERS ONLY PLEASE");
                System.out.println("TRY AGAIN");
                System.out.println("**************************");
                scan.nextLine();
            }
                if (input == 1) {
                    drawCard();
                } else if (input == 2) {
                    moveWasteToFoundation();

                } else if (input == 3) {
                    moveWasteToTableau();

                } else if (input == 4) {
                    movetableauToTableau();

                } else if (input == 5) {
                    TableauToFoundation();

                }

                if (clear(stacks[INDEXES.FH.ordinal()], stacks[INDEXES.FD.ordinal()], stacks[INDEXES.FC.ordinal()], stacks[INDEXES.FS.ordinal()])) {
                    System.out.println("AWESOME YOU WON SOLITAIRE AND AT LIFE TOO");
                    input = 6;
                }
            }
        
        System.out.println("THANKS FOR PLAYING");
    }

    private static ArrayList<String> shuffledDeck(){
        
        ArrayList<String> deck = new ArrayList<>();
        
        String[] suit = new String[] { "H","D","C","S" };        //given code
        for(int i=1; i<=13; i++)
            for(int j=0; j<4; j++)
                deck.add( i + suit[j] );
        
        Collections.shuffle(deck);
        
        return deck;
    }
    
    private static void Start(){
        
        ArrayList<String> deck = shuffledDeck();
        
        stacks[INDEXES.FH.ordinal()].push(deck.remove(deck.indexOf("1H"))); // FOUNDATION OF HEARTS GETTING 1              assume we have this at the start
        stacks[INDEXES.FD.ordinal()].push(deck.remove(deck.indexOf("1D"))); // FOUNDATION OF DIAMONDS GETTING 1            assume we have this from the start
        stacks[INDEXES.FC.ordinal()].push(deck.remove(deck.indexOf("1C"))); // FOUNDATION OF CLUBS GETTING 1               assume we have this from the start
        stacks[INDEXES.FS.ordinal()].push(deck.remove(deck.indexOf("1S"))); // FOUNDATION OF SPADES GETTING 1              assume we have this from the start
        
        EnumSet.range(INDEXES.T0, INDEXES.T6).forEach( (i)-> {
            stacks[i.ordinal()].push(deck.remove(0)); 
            
            for(int j=0; j<i.ordinal(); j++)
                queues[i.ordinal()].add(deck.remove(0));
        });
        
        while(!deck.isEmpty())
                stacks[INDEXES.stock.ordinal()].push(deck.remove(0));
    }
    
    
 private static void drawCard(){
     
            if(!stacks[INDEXES.stock.ordinal()].isEmpty()){        // moving 1 card at a time to waste
            stacks[INDEXES.waste.ordinal()].push(stacks[INDEXES.stock.ordinal()].pop());
            }
            
             else if (stacks[INDEXES.stock.ordinal()].isEmpty()){// putting cards back in stock
                 int maxsize = stacks[INDEXES.waste.ordinal()].size();
                for (int i = 0; i < maxsize; i++) {
                    stacks[INDEXES.stock.ordinal()].push(stacks[INDEXES.waste.ordinal()].pop());
                }
            
            }
 
 }   
    
    
  private static void moveWasteToFoundation(){
  
      
   if(stacks[INDEXES.waste.ordinal()].empty())
   {
      System.out.println("*******************************************************");
      System.out.println("THERE IS NOTHING ON WASTE DRAW A CARD from the deck");
      System.out.println("********************************************************");
   
       return;
   }

   String waster = stacks[INDEXES.waste.ordinal()].peek().toString();
   String wastesuit = waster.substring(waster.length()-1);
   int wastenum = Integer.valueOf( waster.substring(0,waster.length()-1) );
           
  for( INDEXES s: EnumSet.range(INDEXES.FH, INDEXES.FS) )
  {
    String card = stacks[s.ordinal()].peek().toString();
    String cardsuit = card.substring(card.length()-1);
    int cardnum = Integer.valueOf( card.substring(0,card.length()-1) );
  
    if ( cardsuit.equals(wastesuit) && cardnum+1 == wastenum )
    {
        stacks[s.ordinal()].push(stacks[INDEXES.waste.ordinal()].pop());
        return;
    }
    
  }
  
  System.out.println("******************************************************");
  System.out.println("Can't put " +stacks[INDEXES.waste.ordinal()].peek().toString()+" yet");
  System.out.println("********************************************************");
      
  }
  
  private static void moveWasteToTableau(){

    Scanner reader = new Scanner(System.in);
    try{
            int tab;

            if (!stacks[INDEXES.waste.ordinal()].isEmpty()) {
                System.out.println("Move waste card to which tableau? (0-6)");
                tab = reader.nextInt();

                if (tab < 7) {
                    while (tab < 7) {

                        if (stacks[tab].isEmpty()) {

                            stacks[tab].push(stacks[INDEXES.waste.ordinal()].pop());
                            tab = 8;
                        } else if (getNum(stacks[INDEXES.waste.ordinal()]) + 1 == getNum(stacks[tab]) && (isRed(getSuit(stacks[INDEXES.waste.ordinal()])) != isRed(getSuit(stacks[tab])))) {
                            stacks[tab].push(stacks[INDEXES.waste.ordinal()].pop());
                            tab = 8;

                        } else {
                            System.out.println("*****************************************************************************************************");
                            System.out.println("Im sorry this move is not legal  fit " + stacks[INDEXES.waste.ordinal()].peek().toString() + " into " + stacks[tab] + " check the number or color.");
                            System.out.println("*********************************************************************************************************");
                            tab = 7;
                        }
                    }
                } else {
                    System.out.println("******************************************************************************");
                    System.out.println("Wrong input for To tableau. Input has to be between 0-6. You entered: " + tab + " try again");
                    System.out.println("*******************************************************************************");
                    tab = 7;
                }

            } else {
                System.out.println("*******************");
                System.out.println("WASTE IS EMPTY");
                System.out.println("*******************");
            }
        }catch (InputMismatchException e) {
            System.out.println("**************************");//INPUT MISMATCH EXCEPTION IN CASE IT ENTERS A STRING TO ADD ROBUSTNESS
            System.out.println("INTEGERS ONLY PLEASE");
            System.out.println("TRY AGAIN");
            System.out.println("**************************");
            reader.nextLine();
        }

  }

  private static void movetableauToTableau(){
      Scanner reader = new Scanner(System.in);
      Scanner read = new Scanner(System.in);
      try{
          int from, to;
          System.out.println("Move top card FROM which tableau? (0-6)");
          from = reader.nextInt();

          if (from < 7) {
              while (from < 7) {
                  if (!stacks[from].isEmpty()) {
                      System.out.println("TO which tableau? (0-6)");
                      to = read.nextInt();

                      if (to < 7) {
                          while (to < 7) {

                              if (stacks[to].isEmpty()) {
                                  stacks[to].push(stacks[from].pop());
                                  if (!queues[from].isEmpty() && stacks[from].isEmpty()) {
                                      stacks[from].push(queues[from].remove());
                                  }
                                  to = 7;
                                  from = 7;
                              } else if (!(from == to) && getNum(stacks[from]) + 1 == getNum(stacks[to]) && (isRed(getSuit(stacks[from])) != isRed(getSuit(stacks[to])))) {
                                  stacks[to].push(stacks[from].pop());
                                  if (!queues[from].isEmpty() && stacks[from].isEmpty()) {
                                      stacks[from].push(queues[from].remove());
                                  }
                                  from = 7;
                                  to = 7;
                              } else if (to<0 || to>6 ) {
                                  System.out.println("**********************************************");
                                  System.out.println("INVALID NUMBER SELECTED for TO tableau(0-6)");
                                  System.out.println("*********************************************");
                                  from = 7;
                                  to = 7;
                              } else {
                                  System.out.println("***********************");
                                  System.out.println("Can't move top card from " +stacks[from] +"to " + stacks[to]);
                                  System.out.println("************************");
                                  from = 7;
                                  to = 7;
                              }
                          }
                      } else {
                          System.out.println("******************************************************************************");
                          System.out.println("Wrong input for To tableau. Input has to be between 0-6. You entered: " + to + " try again");
                          System.out.println("*******************************************************************************");
                          to = 7;
                          from = 7;
                      }
                  } else {
                      System.out.println("***************************");
                      System.out.println("this stack is empty sorry");
                      System.out.println("****************************");
                      from = 7;
                  }

              }
          } else {
              System.out.println("******************************************************************************");
              System.out.println("Wrong input for from tableau. Input has to be between 0-6. You entered: " + from + " try again");
              System.out.println("*******************************************************************************");

              from = 7;
          }

      }catch (InputMismatchException e) {
          System.out.println("**************************");//INPUT MISMATCH EXCEPTION IN CASE IT ENTERS A STRING TO ADD ROBUSTNESS
          System.out.println("INTEGERS ONLY PLEASE");
          System.out.println("TRY AGAIN ");
          System.out.println("**************************");
          reader.nextLine();
      }

  }

  private static void TableauToFoundation(){
        Scanner reader = new Scanner(System.in);
        try {
            int tableau;
            System.out.println("FROM which TABLEAU do you want to move the TOP card to the foundation (0-6)");
            tableau = reader.nextInt();
            
            if ( tableau>7 ) return;
               
            if(stacks[tableau].empty())
            {
                System.out.println("*******************************************************");
                System.out.println("THAT TABLEAU IS EMPTY");
                System.out.println("********************************************************");
   
                return;
            }

   String tab = stacks[tableau].peek().toString();
   String tabsuit = tab.substring(tab.length()-1);
   int tabnum = Integer.valueOf( tab.substring(0,tab.length()-1) );
           
  for( INDEXES s: EnumSet.range(INDEXES.FH, INDEXES.FS) )
  {
    String card = stacks[s.ordinal()].peek().toString();
    String cardsuit = card.substring(card.length()-1);
    int cardnum = Integer.valueOf( card.substring(0,card.length()-1) );
  
    if ( cardsuit.equals(tabsuit) && cardnum+1 == tabnum )
    {
        stacks[s.ordinal()].push(stacks[tableau].pop());
        return;
    }
    
  }
  
  System.out.println("******************************************************");
  System.out.println("Can't put " +stacks[tableau].peek().toString()+" yet");
  System.out.println("********************************************************");
   
                      
            
        }catch (InputMismatchException e) {
            System.out.println("**************************");//INPUT MISMATCH EXCEPTION IN CASE IT ENTERS A STRING TO ADD ROBUSTNESS
            System.out.println("INTEGERS ONLY PLEASE");
            System.out.println("TRY AGAIN");
            System.out.println("**************************");
            reader.nextLine();
        }

  }



  private static boolean isRed(String suit){
        boolean red;
        if (suit.equals("H") || suit.equals("D")){
            red = true;
            return red;
        }
        else{
            return false;
        }
  }
    private static boolean isBlack(String suit){
        boolean black;
        if (suit.equals("C") || suit.equals("S")){
            black = true;
            return black;
        }
        else{
            return false;
        }
    }

  private static int getNum(Stack stack){                             //life-saver methods to get numbers

      String total = stack.peek().toString();
      String totalnum = total.substring(0,total.length()-1);
      int num = Integer.parseInt(totalnum);
        return num;
  }

  private static String getSuit (Stack stack){                          //life - saver method to get the suit

      String total = stack.peek().toString();
      String suit = total.substring(total.length()-1);

      return suit;
  }

  private static boolean clear(Stack foundation1,Stack foundation2, Stack foundation3,Stack foundation4){
        boolean win = false;
        if (foundation1.size() == 13 && foundation2.size() == 13 && foundation3.size() == 13 && foundation4.size() == 13 ){     // if all the foundation have their 13 respective cards you win
            win = true;
            return win;
        }
        else{
            return win;
        }


  }

  
}
  
  

    
    














































