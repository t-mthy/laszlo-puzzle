/*==============================================================================
Module: Laszlo Puzzle

Author: t-mthy

Description: Discover Laszlo’s reward by solving the IoT puzzle
==============================================================================*/

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LaszloPuzzle {
    static final String CLS = "\033[2J\033[1;1H";
    static final String DFLT = "\033[0m";
    static final String RED = "\033[31;1m";
    static final String GREEN = "\033[32;1m";
    static final String CYAN = "\033[36;1m";
    static final String PURPLE = "\033[35;1m";
    static final String YELLOW = "\033[33;1m";

    static ArrayList<Room> vertex = new ArrayList<Room>();
    static ArrayList<Room> path = new ArrayList<Room>();

    public static void main(String[] args) throws IOException {
        System.out.print(CLS);
        Scanner input = new Scanner(System.in);

        // read in vertex
        Scanner fin = new Scanner(new File("vertex.txt"));
        String node = "";
        while (fin.hasNextLine()) {
            node = fin.nextLine();
            vertex.add(new Room(node));
        }
        System.out.println(vertex.size() + " vertices read from file.");

        // read in edge
        fin = new Scanner(new File("edge.txt"));
        String from, direction, to;
        int count = 0;
        while (fin.hasNext()) {
            count++;
            from = fin.next();
            direction = fin.next();
            to = fin.next();
            // Uncomment below to help debug runtime error reading edge txt
            // System.out.println("From:" + from + " Direction:" + direction + " To:" + to);

            // locate idx for "From Vertex" in ArrayList
            int indexFrom = 0;
            while (!vertex.get(indexFrom).roomName.equals(from)) {
                indexFrom++;
            }

            // locate idx for "To Vertex" in ArrayList
            int indexTo = 0;
            while (!vertex.get(indexTo).roomName.equals(to)) {
                indexTo++;
            }

            // create edge
            if (direction.equals("North")) {
                vertex.get(indexFrom).north = vertex.get(indexTo);
                vertex.get(indexTo).south = vertex.get(indexFrom);
            }
            if (direction.equals("South")) {
                vertex.get(indexFrom).south = vertex.get(indexTo);
                vertex.get(indexTo).north = vertex.get(indexFrom);
            }
            if (direction.equals("East")) {
                vertex.get(indexFrom).east = vertex.get(indexTo);
                vertex.get(indexTo).west = vertex.get(indexFrom);
            }
            if (direction.equals("West")) {
                vertex.get(indexFrom).west = vertex.get(indexTo);
                vertex.get(indexTo).east = vertex.get(indexFrom);
            }
        }
        System.out.println(count + " edges read from file.");

        // intro
        System.out.println("\n\n" + PURPLE);
        System.out.println("    __                     __         ____                  __   ");
        System.out.println("   / /   ____ __________  / /___     / __ \\__  __________  / /__ ");
        System.out.println("  / /   / __ `/ ___/_  / / / __ \\   / /_/ / / / /_  /_  / / / _ \\");
        System.out.println(" / /___/ /_/ (__  ) / /_/ / /_/ /  / ____/ /_/ / / /_/ /_/ /  __/");
        System.out.println("/_____/\\__,_/____/ /___/_/\\____/  /_/    \\__,_/ /___/___/_/\\___/ ");
        System.out.print(CYAN + "\n\nUncover the hidden password within the IoT devices");
        System.out.println(" and claim your reward!\n" + DFLT);
        System.out.println("\n\nPress <Enter> to continue...");
        input.nextLine();

        // user input/movements
        Room start = vertex.get(0);
        Room cur = start; // the current "temp" pointer
        char choice = ' ';
        Room goal = vertex.get(vertex.size() - 1);
        Room pwRoom = vertex.get((int) (Math.random() * vertex.size()));
        String password = "            ?             ";
        boolean gotPW = false;
        int idxNum; // for Dijkstra
        String target = ""; // for Dijkstra
        Room targetRoom; // for Dijkstra

        while (choice != 'q') {
            System.out.print(CLS);
            System.out.println("You're connected to the: " + YELLOW + cur.roomName + DFLT);

            // got pw in random room/device
            if (cur == pwRoom) {
                password = "definitely not password123";
                gotPW = true;
            }
            System.out.println("Crypto wallet password: " + GREEN + "[" + password + "]\n\n" + DFLT);

            // ASCII arts
            if (cur.roomName.equals("ONT"))
                drawONT();
            if (cur.roomName.equals("router"))
                drawRouter();
            if (cur.roomName.equals("WAP1"))
                drawWAP();
            if (cur.roomName.equals("WAP2"))
                drawWAP();
            if (cur.roomName.equals("WAP3"))
                drawWAP();
            if (cur.roomName.equals("doorbell"))
                drawDoorbell();
            if (cur.roomName.equals("cam"))
                drawCam();
            if (cur.roomName.equals("TV"))
                drawTV();
            if (cur.roomName.equals("toilet"))
                drawToilet();
            if (cur.roomName.equals("phone"))
                drawPhone();
            if (cur.roomName.equals("printer"))
                drawPrinter();
            if (cur.roomName.equals("lights"))
                drawLights();
            if (cur.roomName.equals("vacuum"))
                drawVacuum();
            if (cur.roomName.equals("garage"))
                drawGarage();
            if (cur.roomName.equals("AC"))
                drawAC();
            if (cur.roomName.equals("fridge"))
                drawFridge();
            if (cur.roomName.equals("oven"))
                drawOven();
            if (cur.roomName.equals("microwave"))
                drawMicrowave();
            if (cur.roomName.equals("firewall"))
                drawFirewall();
            if (cur.roomName.equals("laptop"))
                drawLaptop();
            if (cur.roomName.equals("wallet"))
                drawWallet();

            // arrived at wallet but NO password
            if (cur == goal && !gotPW)
                System.out.println(RED + "\n\nYou still need the password to get the goodies!" + DFLT);
            // win screen
            if (cur == goal && gotPW) {
                System.out.print(CLS + YELLOW);
                winScreen();
                System.out.println(GREEN + "\n\nCongratulations! You just got 10,000 BTCs!\n\n" + DFLT);
                break;
            }

            // show allowed movements
            System.out.print("\n\nYou can move:  " + PURPLE);
            if (cur.north != null)
                System.out.print("North  ");
            if (cur.west != null)
                System.out.print("West  ");
            if (cur.south != null)
                System.out.print("South  ");
            if (cur.east != null)
                System.out.print("East  ");
            System.out.println(DFLT);

            // user input
            System.out.print("Commands:");
            System.out.print(CYAN + "  w" + DFLT + " = North  ");
            System.out.print(CYAN + "  a" + DFLT + " = West  ");
            System.out.print(CYAN + "  s" + DFLT + " = South  ");
            System.out.print(CYAN + "  d" + DFLT + " = East  ");
            System.out.print(CYAN + "  f" + DFLT + " = Shortest Path  ");
            System.out.print(CYAN + "  q" + DFLT + " = Quit  ");
            System.out.print("\nYour choice: ");
            choice = Character.toLowerCase(input.next().charAt(0));

            // movement & search
            if (choice == 'w' && cur.north != null)
                cur = cur.north;
            if (choice == 'a' && cur.west != null)
                cur = cur.west;
            if (choice == 's' && cur.south != null)
                cur = cur.south;
            if (choice == 'd' && cur.east != null)
                cur = cur.east;
            if (choice == 'f') {
                System.out.print("Which device would you like to connect to?  ");
                input.nextLine();
                target = input.nextLine();

                // search for target
                idxNum = 0;
                while (idxNum < vertex.size() && !vertex.get(idxNum).roomName.equals(target))
                    idxNum++;

                // if NOT found...idxNum incremented to the same as len(list)
                if (idxNum >= vertex.size())
                    System.out.println("\nUnknown device: " + RED + target + DFLT + " does not exist.");
                else {
                    targetRoom = vertex.get(idxNum);
                    dijkstra(cur, targetRoom);

                    // same node is distance zero
                    if (targetRoom.distance == 0)
                        System.out.println("\nYou're already connected to the " + target + ".");
                    // if target FOUND, print path
                    else {
                        System.out.print("\nShortest Path found:  " + PURPLE);

                        for (int i = 0; i < path.size(); i++)
                            System.out.print(path.get(i).roomName + "  ");

                        System.out.print(GREEN + "\n[Connect through " + targetRoom.distance);
                        System.out.println(" device(s) to get to the " + target + "]" + DFLT);
                    }
                }

                System.out.println("\n\nPress <Enter> to continue...");
                input.nextLine();
            }
        } // end user input/movements loop

        input.close();
    } // end main()

    static void dijkstra(Room start, Room finish) {
        // set distance to all rooms (except for start) to 9000 and visited = false
        for (int i = 0; i < vertex.size(); i++) {
            if (vertex.get(i) == start)
                vertex.get(i).distance = 0;
            else
                vertex.get(i).distance = 9000; // set distance to "infinity"

            vertex.get(i).visited = false;
        }

        // do Dijkstra - find distance to each room
        Room cur = start;
        while (!finish.visited) {
            cur.visited = true;
            if (cur.north != null && !cur.north.visited && cur.north.distance > cur.distance + 1)
                cur.north.distance = 1 + cur.distance;
            if (cur.south != null && !cur.south.visited && cur.south.distance > cur.distance + 1)
                cur.south.distance = 1 + cur.distance;
            if (cur.east != null && !cur.east.visited && cur.east.distance > cur.distance + 1)
                cur.east.distance = 1 + cur.distance;
            if (cur.west != null && !cur.west.visited && cur.west.distance > cur.distance + 1)
                cur.west.distance = 1 + cur.distance;

            int smallest = 9000;
            int smallestIndex = 0;
            for (int i = 0; i < vertex.size(); i++) {
                if (!vertex.get(i).visited && vertex.get(i).distance < smallest) {
                    smallest = vertex.get(i).distance;
                    smallestIndex = i;
                }
            }
            cur = vertex.get(smallestIndex);
        }

        // populate "path" ArrayList with Rooms of shortest path
        cur = finish;
        path.clear();
        path.add(0, finish);
        while (cur != start) {
            int N = 9000, S = 9000, E = 9000, W = 9000;

            if (cur.north != null)
                N = cur.north.distance;
            if (cur.south != null)
                S = cur.south.distance;
            if (cur.east != null)
                E = cur.east.distance;
            if (cur.west != null)
                W = cur.west.distance;
            if (N < S && N < E && N < W)
                cur = cur.north;
            else if (S < E && S < W)
                cur = cur.south;
            else if (E < W)
                cur = cur.east;
            else
                cur = cur.west;

            path.add(0, cur);
        }
    } // end dijkstra()

    static void drawONT() {
        System.out.println("    .::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::     ");
        System.out.println("   :^:                                                                   .:^.   ");
        System.out.println("  .^.                                                                      :^   ");
        System.out.println("  .^.                    7JJJ     JY  ?Y   7JYYJJ                          :^   ");
        System.out.println("  .^.                   YG  !G    5GP YG     5G                            :^   ");
        System.out.println("  .^.                   YG  7G    PP 5GG     5G               ...........  :^   ");
        System.out.println("  .^.                    !JJ?     ?J  ?J     ?J         .................  :^   ");
        System.out.println("  .^.                                            ........................  :^   ");
        System.out.println("  .^.                                 ...................................  :^   ");
        System.out.println("  .^.                   ... .............................................  :^   ");
        System.out.println("  .^.                  .7?:    ~7~    .77.    ~7^                  .77.    :^   ");
        System.out.println("  .^.         ^?J?J?^.^^^^^^. :::::  .::::.  :::::               .?5555?.  :^   ");
        System.out.println("  .^.        .~PGGGP~.^^^^^^: ^^^^^. :^^^^: .^^^^^               .P@@@@P.  :^   ");
        System.out.println("  .^.        .~PGGGG~.^^^^^^: ^^^^^. :^^^^: .^^^^^               .P@@@@P.  :^   ");
        System.out.println("  .^:         :??YY?:.:~!!^:. .::::  .::::.  ::::.               .7P&@P7   :^   ");
        System.out.println("   .^:.          ~~   .!Y?.                                        ^#@^ ..:^.   ");
        System.out.println("     .::::::::::^!~::::!Y?:::::::::::::::::::::::::::::::::::::::::~&@~:::.     ");
        System.out.println("               .!!.    ~Y?                                         :#@:         ");
        System.out.println("               ~!:     ~Y?                                         :#@:         ");
    }

    static void drawRouter() {
        System.out.println("        .J!                                           !J.        ");
        System.out.println("        7@@.                                         .@@7        ");
        System.out.println("        7@@.           .^7YPGB#####BGPY7^.           .@@7        ");
        System.out.println("        7@@.       ~5#&&#GY7~^^:::^^~7YG#&&#5~       .@@7        ");
        System.out.println("        7@@.    !#@&P!.  .~?5PGBBBGP5?~.  .!P&@B!    .@@7        ");
        System.out.println("        7@@.    ?Y:  :Y#&&B5?!^^^^^!?5B&&#Y:  :YJ    .@@7        ");
        System.out.println("        7@@.        5@B!.  .~?Y5P5Y?~.  .!B@5        .@@7        ");
        System.out.println("        7@@.            ?#@&#G5J?JYG#&@#?            .@@7        ");
        System.out.println("        !@@            .PY:    .:.    :YP.            @@!        ");
        System.out.println("      .7B@@5~                .#&P&#.                ~5@@B7.      ");
        System.out.println("      #@P77&@7               ^@#~#@^               7@&77P@#      ");
        System.out.println("      #@:  5@?                .7Y7.                ?@5  :@#      ");
        System.out.println("      &@^  P@J                                     J@P  ^@&      ");
        System.out.println(" .5&&&&@&##&@&#&&&&&&&&&&&&&&&&&&&&&&&&&&&&##&&&&&#&@&##&@&#&&5. ");
        System.out.println(" Y@5                                                         5@Y ");
        System.out.println(" 5@?                                     .YBBG~   .5BBG~     ?@5 ");
        System.out.println(" 5@?    ?@@G  7@@G  7@@G                :@@^.G@5 :@@^.G@Y    ?@5 ");
        System.out.println(" 5@?    ^GG7  :GB7  :GB7                .&@5J&@! .&@5J&@!    ?@5 ");
        System.out.println(" 5@7                                      :??~     :??~      7@5 ");
        System.out.println(" ?@#~~~~~~~^~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~^^~~~~~~~^^^~~~~~~#@? ");
        System.out.println("  ^5GGGGGG@@&PGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGP&@@GGGGGG5^  ");
        System.out.println("          5&^                                       ^&5          ");
    }

    static void drawWAP() {
        System.out.println("                   .........                   ");
        System.out.println("             ..                ...             ");
        System.out.println("         ...                       ..          ");
        System.out.println("       ..                             ..       ");
        System.out.println("     ..                                 ..     ");
        System.out.println("    .                .....               ..    ");
        System.out.println("  ..             .:::.....::::.           ..   ");
        System.out.println("  .            ^^.           .:^.          ..  ");
        System.out.println(" .           .~.               .~:          .. ");
        System.out.println("..          .!.     .......      ~:         .. ");
        System.out.println("..          ^^         ..        .~         ...");
        System.out.println("..          ^^    .... . .. .    .~         ...");
        System.out.println(" .          .!    :..: . .. :    ~:         .. ");
        System.out.println(" .           :~.                ^^          .. ");
        System.out.println("  .           .^:.            :^.          ..  ");
        System.out.println("   .            .:::.......:::.           ..   ");
        System.out.println("    .               .......              ..    ");
        System.out.println("     ..                                ...     ");
        System.out.println("       ..                            ...       ");
        System.out.println("          ..                     ....          ");
        System.out.println("              ....         .......             ");
        System.out.println("                   .........                   ");
    }

    static void drawDoorbell() {
        System.out.println(". J&GGGGGGGGGGGGGGGGGGGGGG&! ~");
        System.out.println(". G@                      @J ~");
        System.out.println(". P&      .~?YYYY?^       @J ~");
        System.out.println(". P&    .GBJ7^...^JBP.    @J ~");
        System.out.println(". P&   ^@7.&@@#     Y@.   @J ~");
        System.out.println(". P&   &G  G&&P      #B   @J ~");
        System.out.println(". P&   #B            &G   @J ~");
        System.out.println(". P&   .&5         .G&.   @J ~");
        System.out.println(". P&     YB5!^::^7PB?     @J ~");
        System.out.println(". P&       .~7??7~.       @J ~");
        System.out.println(". P@                      @J ~");
        System.out.println(". P@&&&&&&&&&&&&&&&&&&&&&&@J ~");
        System.out.println(". P@                      @J ~");
        System.out.println(". P&          :.          @J ~");
        System.out.println(". P&        .Y##?         @J ~");
        System.out.println(". P&       !@@@@@@^       @J ~");
        System.out.println(". P&       &@@@@@@B       @J ~");
        System.out.println(". P&      .@@@@@@@@       @J ~");
        System.out.println(". P&     ^&@@@@@@@@#:     @J ~");
        System.out.println(". P&     5BGG#@@BGGBY     @J ~");
        System.out.println(". P&          ?7          @J ~");
        System.out.println(". G@                      @J ~");
        System.out.println(". J&GGGGGGGGGGGGGGGGGGGGGG&! ~");
    }

    static void drawCam() {
        System.out.println(" Y#J777777777777777777777777777777777777777777P#^ ");
        System.out.println(".@!                                            B# ");
        System.out.println(":@~                                            G# ");
        System.out.println(":@~                                            B# ");
        System.out.println(" B#~::::::::::::::::::::::::::::::::::::::::::?&7 ");
        System.out.println("  ^B@5JJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJB@J.  ");
        System.out.println("   7@                                       7@    ");
        System.out.println("   7@.            :!J55PPPP5Y?~.            7@.   ");
        System.out.println("   7@.        .7GG57^.     ..~?PB5^         7@.   ");
        System.out.println("   7@.      .5#Y:    :!7??7~.    ~G#7       7@.   ");
        System.out.println("   7@.     !&Y   .?GGJ!^^^~75GP~   :B#.     7@.   ");
        System.out.println("   7@.    ?@^   ?&Y. .^~!~^.  ^G#^   5@.    7@.   ");
        System.out.println("   ^@:   ^@~   P&. .YY??P##G5?  7@~   G&    5&    ");
        System.out.println("    &5   B#   ~@: .#Y    B@@BPP  Y@   .@~   @Y    ");
        System.out.println("    ~@^  &P   Y&  ?GG&?!Y@@@@?&. ^@:   @J  P&     ");
        System.out.println("     J@: &P   ~@: .BY&@@@@YBBPP  5&    @? Y@:     ");
        System.out.println("      7&7G&    P&. .YPG###BG57  7@~   ^@!G&.      ");
        System.out.println("       :B&@?    ?&Y.  :^~~^.  ^B#:    #@&Y        ");
        System.out.println("         ^B@Y.    7GGY7~^^~?5GP^    :#@5.         ");
        System.out.println("           .JBG?^.   :~!77!^.   .~YBG7            ");
        System.out.println("              .!YPPPY?7!!!!7J5PPPJ^.              ");
        System.out.println("                   .:^~!!!!~^..                   ");
    }

    static void drawTV() {
        System.out.println("            Y@&~                                  ");
        System.out.println("            :G@@&!              J&#.              ");
        System.out.println("              .P@@&7         .Y@@@J               ");
        System.out.println("                .5@@@?     .5@@&7                 ");
        System.out.println("                  .J@@@J..P@@&!                   ");
        System.out.println("                     5@@@@@@?                     ");
        System.out.println(" :G@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@G^ ");
        System.out.println("?@@@5~^::^^^^^^^^^^^^^^^^^^^^:::^^^^^^^^^^^^^5@@@J");
        System.out.println("&@@G   :5PPPPPPPPPPPPPPPPPPPPP!               P@@@");
        System.out.println("&@@P  ?@!                    :&B     ~?J?:    5@@@");
        System.out.println("&@@G  &G                      ~@:  !@B?!J&&:  P@@@");
        System.out.println("&@@G  @5                      ^@^  @B     @#  P@@@");
        System.out.println("&@@G  @5                      ^@^  P@?..:P@!  P@@@");
        System.out.println("&@@G  @5                      ^@^   ^5GGGJ.   P@@@");
        System.out.println("&@@G  @5                      ^@^     :^.     P@@@");
        System.out.println("&@@G  @5                      ^@^  :B&G5G&5   P@@@");
        System.out.println("&@@G  @5                      :@^  @&    .@G  P@@@");
        System.out.println("&@@G  B&                      5@.  &@:   !@5  P@@@");
        System.out.println("&@@P   B#?~^^^^^^^^^^^^^^^^^!G&^    J#BB#B!   5@@@");
        System.out.println("&@@#    .~??????????????????!:                B@@&");
        System.out.println(":&@@&P55YYYYYYYYYYYYYYYYYYYYYY55555555555555P&@@&^");
        System.out.println("  !G&@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&B7  ");
    }

    static void drawToilet() {
        System.out.println("              .JJJJ.               ");
        System.out.println("   G&&&&&&&&&&&@@@@&&&&&&&&&&&P    ");
        System.out.println("    &@@@@@@@@@@@@@@@@@@@@@@@@&     ");
        System.out.println("    &@@@@@@@@@@@@@@@@@@@@@@@@&     ");
        System.out.println("    &@@@@@@@@@@@@@@@@@@@@@@@@&     ");
        System.out.println("    @@@@@@@@@@@@@@@@@@@@@@@@@&     ");
        System.out.println("    G@@@@@@@@@@@@@@@@@@@@@@@@P     ");
        System.out.println("     Y@@@@@@@@@@@@@@@@@@@@@@Y      ");
        System.out.println("      .7G#@@@@@@@@@@@@@@#P!.       ");
        System.out.println("          G@@@@@@@@@@@@P           ");
        System.out.println("     .!5GB#&#BBGGGGBB#&#BG5!.      ");
        System.out.println("   !&@@5YPG            GPJ5@@&!    ");
        System.out.println("   G@@@                    @@@P    ");
        System.out.println("   .PGB                    BG5.    ");
        System.out.println("    ^&&BGP              PGB&&^     ");
        System.out.println("     .B@@@@@@@@@@@@@@@@@@@@G.      ");
        System.out.println("       ^&@@@@@@@@@@@@@@@@#^        ");
        System.out.println("         ~&@@@@@@@@@@@@&~          ");
        System.out.println("          J@@@@@@@@@@@@J           ");
        System.out.println("          &@@@@@@@@@@@@&           ");
        System.out.println("         :@@@@@@@@@@@@@@.          ");
        System.out.println("         ^@@@@@@@@@@@@@&^          ");
    }

    static void drawPhone() {
        System.out.println(" G@@@@@@@@@@@GPPPPPPP@@@@@@@@@@@&: ");
        System.out.println("^@@@#&#######5YYYYYYYGBBBBBBBB#@@5 ");
        System.out.println("^@@?                           @@5 ");
        System.out.println("^@@?                          .@@5 ");
        System.out.println("^@@?                          .@@] ");
        System.out.println("^@@?                          .@@] ");
        System.out.println("^@@?                          .@@5 ");
        System.out.println("^@@?                          .@@5 ");
        System.out.println("^@@?                          .@@] ");
        System.out.println("^@@?                          .@@] ");
        System.out.println("^@@?                          .@@5 ");
        System.out.println("^@@?                          .@@] ");
        System.out.println("^@@?                          .@@] ");
        System.out.println("^@@?                          .@@5 ");
        System.out.println("^@@?                          .@@5 ");
        System.out.println("^@@?                          .@@5 ");
        System.out.println("^@@?                          .@@5 ");
        System.out.println("^@@?                           @@5 ");
        System.out.println("^@@&GBBBBBBBBB##BB##BBBBBBBBBB#@@5 ");
        System.out.println("^@@@@@@@@@@@@@@@:.:@@@@@@@@@@@@@@5 ");
        System.out.println("^@@@@@@@@@@@@@@     @@@@@@@@@@@@@P ");
        System.out.println(".#@@@@@@@@@@@@@@:.:@@@@@@@@@@@@@@. ");
    }

    static void drawPrinter() {
        System.out.println("              .BP5555555555555555555555B~              ");
        System.out.println("              :@:                      &?              ");
        System.out.println("              .@.                      #?              ");
        System.out.println("            .7P@.                      ##?:            ");
        System.out.println("           ~&!~@.                      #Y:BY           ");
        System.out.println("           BP :@.                      #? ~@           ");
        System.out.println("           BP :@.                      #? ~@           ");
        System.out.println("           BP :@.                      #? ~@           ");
        System.out.println("           BP :@.                      #? ~@           ");
        System.out.println("           BP :@.                      #? ~@           ");
        System.out.println("  ~YYYYYYYJ#BJ5&5YYYYYYYYYYYYYYYYYYYYYY#GJP&YYJYYYYY7  ");
        System.out.println(" 5B.                                         ...   .Y# ");
        System.out.println(" &!                 :::::::::::::::.        ?PJG!   .@:");
        System.out.println(" &!                .77777777777777?^        GG~#5   .@:");
        System.out.println(" &!                                          :~:    .@:");
        System.out.println(" &!     .YJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJY:     .@:");
        System.out.println(" &!     :@~::::.::::::::::::::::::::::::.::::&J     .@:");
        System.out.println(" &?     .@Y???5&YJJJJJJJJJJJJJJJJJJJJJJG&J???&?     .@:");
        System.out.println(" ~#?~~~^?@!^^^B5                       .&J^^^&G^~~~7BY ");
        System.out.println("   ^~~~~~~~~~##                         :@Y~~~~~~~~^.  ");
        System.out.println("            ~&.                          !#            ");
        System.out.println("           .&G!777777777777777777777777777&P           ");
        System.out.println("            ::^^^^^^^^^^^^^^^^^^^^^^^^^^^^:.           ");
    }

    static void drawLights() {
        System.out.println("         ^!7?77!!!!7777!^         ");
        System.out.println("      ~?7^.         ....^7?^      ");
        System.out.println("    7J^             .!77:  ^J?    ");
        System.out.println("  ^P^                  .~J7  :P~  ");
        System.out.println(" !P                       :J.  P! ");
        System.out.println(":G                              B:");
        System.out.println("G~                              75");
        System.out.println("#.                              ~G");
        System.out.println("G~                              57");
        System.out.println("^G                             ~P ");
        System.out.println(" !BJ!!7PJ!!!!!!75?!!!!!!?P7!!7G5  ");
        System.out.println("  :P~  :P~      B:     :P~  :5!   ");
        System.out.println("    7Y.  JY     B:    ?Y   ?Y.    ");
        System.out.println("     .57  ^G    B.   P!  :P^      ");
        System.out.println("       57  ?Y   B.  7Y  :B.       ");
        System.out.println("        B  :G   B.  P~  P~        ");
        System.out.println("        BJ!?G!!!G?!!GJ!!#.        ");
        System.out.println("        B:              B.        ");
        System.out.println("        G!.:::::::::::.:#.        ");
        System.out.println("        G7:::::::::::::^#         ");
        System.out.println("        !5             ?Y         ");
        System.out.println("         :J7^::::::::!J~          ");
        System.out.println("            :P~::::7?.            ");
    }

    static void drawVacuum() {
        System.out.println("                                ..::^^^~~~~~~^^^::..                                 ");
        System.out.println("                       .:~7?JYYYYJJ???77?5PY77???JJYYYYYJ7!^..                       ");
        System.out.println("                 .^7J55Y?7~^^::.....:::::^^:::::.....::^^~7JY55Y?~.                  ");
        System.out.println("              ^JPP57~:.......:~!?JYY55YYYYYYYY55YYJ?7~^:......:^7YPGY~.              ");
        System.out.println("           ^YBG?^.......:~?Y55Y?!^:.....    .....:^~7JY55J!:.......:7PBP!            ");
        System.out.println("         ~G#P^.        :YY!^.    .:^!77???????7!~^:.   .:~J5~         :Y#B?          ");
        System.out.println("       .5##!                .^7J5P5YYYYYYYYYYYYYYY5P5J!:.               :G&B:        ");
        System.out.println("       G##~               :7Y55555YYY5YJ??77?JY55YY5555P5!.              .B##:       ");
        System.out.println("      ^##B               ^YY5YJJYYYJ?:...^!^...:?YYYYJJ55YJ:              Y&&J       ");
        System.out.println("      .##B:              7J?Y5JYYYJJ7   .^?^.   7JJYJYJ55JY^              P#&!       ");
        System.out.println("       5&#P.             .7JYYJJYYJJJ?!~^^::^~!?JJJJJJJYYJ~              ?##&.       ");
        System.out.println("       J@&&B~              :!?JYYYYYYYYY5YYJY5YJJJYYYYJ?~.             :5#&@B        ");
        System.out.println("       J@&&&#P!.              .:^!7?JJYYYYYYYYJJJ?7~^..              ^5#&&&@G        ");
        System.out.println("       ^@&&&&&&B5!:.                 ...........                 .~JG#&&&&&@J        ");
        System.out.println("        ?&&&&&&&&&#GY7^:.                                  ..^!JPB&&&&&&&&@P         ");
        System.out.println("   ......J&&&&&&&&&&&&#BGPY?!~^::......^5PPP!.......::^!7J5GB#&&&&&&&&&&@&P:......   ");
        System.out.println(" ~J7!~~~7?YP#@@@&&&&&&######BBBBGGPPP5PBBYJG#P55PPGGBBBB#######&&&&&@@&&B5Y?7~~~!7J~ ");
        System.out.println("    .~?J?^:?5^?B&@&&&&####BBBBGGGGGPPPP55P555PPPPPGGGGGBBB####&&&&@&#Y:.YJ:^?J?~.    ");
        System.out.println("   .?!:  ~G?    .^JG#&&&&###BBBGGGGGPPPPPPPPPPPPPGGGGBBB###&&&&#GY~.     7G~  .!?.   ");
        System.out.println("       :GG:          .^!J5PGBBBBBBBGGGGGGGGGGGGGGBBBBBBBBG5J7^.           .GG:       ");
        System.out.println("       .~                   ..:^~!7?JJYYYYYYYYJJ?7!~~^..                    ~.       ");
    }

    static void drawGarage() {
        System.out.println("                      .^??^.                      ");
        System.out.println("                  .:!J5PPPP5J7:.                  ");
        System.out.println("              .:!J5PPPP5555PPPP5J!:.              ");
        System.out.println("           :~J5PPPPP5YJ?777JY55PPPP5J!:           ");
        System.out.println("       .~?5PPPPP5YJ?7!!!!!!!!7?JY5PPPPP5?~:       ");
        System.out.println("   .~?YPPPPP5YJ?7!!!!!!!!!!!!!!!!7?JY5PPPPP5?~.   ");
        System.out.println(" 7YPPPP55YJ77!!!!!!!!!!!!!!!!!!!!!!!!77JY55PPPPY?.");
        System.out.println(" 7PP5555YJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJJY5555PP? ");
        System.out.println("  ^~5555Y????????????????????????????????Y5555!^  ");
        System.out.println("   :5555YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY5555:   ");
        System.out.println("   :5555Y????????????????????????????????Y5555^   ");
        System.out.println("   :5555Y77777777777777777777777777777777J5555^   ");
        System.out.println("   :55557        .:^^^^^^^^^^^^^.        !5555^   ");
        System.out.println("   :55557      .7Y?777777777777?Y?.      !5555^   ");
        System.out.println("   :55557     .5?::::::::::::::::?5.     !5555^   ");
        System.out.println("   :55557    .?5^::::::::::::::::^5J.    !5555^   ");
        System.out.println("   :55557   .PPPP55YJJJ????JJJY55PPPP.   !5555^   ");
        System.out.println("   :55557   .PPJ^^?PGGGGGGGGGGPY~^7PP.   !5555^   ");
        System.out.println("   :55557   .PG!  :PB########BP7  .PP:   !5555^   ");
        System.out.println("   :55557   .JY555PPYYYYYYYYYYPP55PYY.   !5555^   ");
        System.out.println("   :55557      ?&##B.         G&#&5      75555^   ");
        System.out.println("   .7777~      .!!7~          ^7!7:      ^7777.   ");
    }

    static void drawAC() {
        System.out.println("   .!P#&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&#G?:   ");
        System.out.println("  5@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@G. ");
        System.out.println(" B@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@&.");
        System.out.println("^@@@@@@@@@@@@&GJ~^:....^~?P&@@@@@@@@@@@@@#############@@@@@Y");
        System.out.println("^@@@@@@@@@&?.               .?#@@@@@@@@@@?!!!!!!!!!!!!#@@@@Y");
        System.out.println("^@@@@@@@#~  ....        P@B?.  ^#@@@@@@@@#BBBBBBBBBBBB&@@@@Y");
        System.out.println("^@@@@@@Y  :B@@@@B~     .@@@@@B^  J@@@@@@@J7?7?7?7??7?7#@@@@Y");
        System.out.println("^@@@@@J  ^@@@@@@@@G    #@@@@@@@^  ?@@@@@@BGGGGGGGGGGGG&@@@@Y");
        System.out.println("^@@@@#   &@@@@@@@@@B  B@@@@@@@&.   B@@@@@5JJJJJJJJJJJJ&@@@@Y");
        System.out.println("^@@@@J  ^&GPPPPGB#&&!G@@@@@&G7     ?@@@@@GPPPPPPPPPPP5&@@@@Y");
        System.out.println("^@@@@J            .P&^.:...        ?@@@@@P555555555555&@@@@Y");
        System.out.println("^@@@@#           J@@@B             B@@@@@5YYYYYYYYYYYJ&@@@@Y");
        System.out.println("^@@@@@J         B@@@@@B           7@@@@@@GPPPPPPPPPPPP&@@@@Y");
        System.out.println("^@@@@@@J       !@@@@@@@&:        ?@@@@@@@Y????????????#@@@@Y");
        System.out.println("^@@@@@@@#^     .&@@@@@@@@Y.    ^B@@@@@@@@BGBBBBBBBBBGG&@@@@Y");
        System.out.println("^@@@@@@@@@#?.    !YPPY?~:.  .7#@@@@@@@@@@J77777777777!#@@@@Y");
        System.out.println("^@@@@@@@@@@@@&P?~... ...^7P#@@@@@@@@@@@@@#BBBBBBBBBBBB&@@@@Y");
        System.out.println(" #@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@.");
        System.out.println(" .P@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@B: ");
        System.out.println("   .7PB##############################################BG?:   ");
        System.out.println("       !????~                                  :?????       ");
        System.out.println("       ?YYYY!                                  ^YYYYJ.      ");
    }

    static void drawFridge() {
        System.out.println(" ~JJ?????777777777777?????JJ: ");
        System.out.println("?P.                        .B:");
        System.out.println("#^                       ^^ J5");
        System.out.println("&.                      .!7.!B");
        System.out.println("&.                      .!7 ~B");
        System.out.println("&.                       J? !B");
        System.out.println("&^                       ^: J5");
        System.out.println("J5.......................  .B^");
        System.out.println(".#J!777777777777777777777!!5B ");
        System.out.println("YY                          B~");
        System.out.println("B!                      .77 YJ");
        System.out.println("#^                      .!! ?P");
        System.out.println("&:                      .77 !G");
        System.out.println("&.                       ?? ~B");
        System.out.println("&.                       JJ ~B");
        System.out.println("&.                       ^^ ~B");
        System.out.println("&.                          ~B");
        System.out.println("&.                          ~B");
        System.out.println("&:                          7G");
        System.out.println("B!                          YY");
        System.out.println("YY                          B~");
        System.out.println(".P7!????!~!!!!!!!!!!~7???7~?5 ");
        System.out.println("  .^@@@@J            P@@@&:.  ");
    }

    static void drawOven() {
        System.out.println("    G&########BBBBBBB########&#    ");
        System.out.println("    &@~ .. .. !YJJJYY........@@.   ");
        System.out.println("    &@. G: JY G&YYY&@.~P  G: @@.   ");
        System.out.println("    &@!.:..::.?GPPPPP:.:..:.^@@    ");
        System.out.println("   .@@BB#&&&##BGGGGGB#&&&#BBB@@:   ");
        System.out.println("  ^@#!:^YGBBBP~^^^^^JGBBBP?^^!#@!  ");
        System.out.println(" Y@P^^!P##&&#BP!^^^?G##&&#BY^^^5@G.");
        System.out.println("Y@@####&&&&&&&&#####&&&&&&&&####&@#");
        System.out.println("5@?.!!!!!!!!!!!!!!!!!!!!!!!!!!!.^@&");
        System.out.println("5@! ??????JJJJJJJJJJJJJJJ?????? .@&");
        System.out.println("5@!     .G5?JJJJJJJJYYYY5G:     .@&");
        System.out.println("5@!     !@^:^^^~~!~~!!!77&J     .@&");
        System.out.println("5@!     ~&^^^~7???J?777??&J     .@&");
        System.out.println("5@!     ~&~!7???JJYYYYYYJ&J     .@&");
        System.out.println("5@!     ~@JJJJYY55555YYJJ&J     .@&");
        System.out.println("5@!     .GP5555PPPP555555B^     .@&");
        System.out.println("5@!                             .@&");
        System.out.println("5@&BBBBBBBBBBBBBBBBBBBBBBBBBBBB#&@&");
        System.out.println("5@Y^???????????????????????????^7@&");
        System.out.println("5@~ !!!!!!!!!!!!!!!!!!!!!!!!!!! .@&");
        System.out.println("5@J.............................~@&");
        System.out.println("?&&##############################&G");
    }

    static void drawMicrowave() {
        System.out.println(" 7B&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&##&&&&&&&&&&&G~ ");
        System.out.println("J@&!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!~P@&~~~~~~~~~~!@@~");
        System.out.println("P@P                                                  7@#...........&@7");
        System.out.println("P@P     :^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^:.          7@@&&&&&&&&&&&@@7");
        System.out.println("P@P   ~&@&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&@@?    ^.   7@#...........&@7");
        System.out.println("P@P   G@Y                                ~@&   ?@G   7@#           &@7");
        System.out.println("P@P   G@J                                ~@&   J@B   7@#  .JGBG?.  &@7");
        System.out.println("P@P   G@J                                ~@&   J@B   7@# ~@@Y!Y@@^ &@7");
        System.out.println("P@P   G@J                                ~@&   J@B   7@# Y@#   #@J &@7");
        System.out.println("P@P   G@J                                ~@&   J@B   7@#  5@@&@&Y  &@7");
        System.out.println("P@P   G@J                                ~@&   J@B   7@#    .:.    &@7");
        System.out.println("P@P   G@J                                ~@&   J@B   7@#    ::.    &@7");
        System.out.println("P@P   G@J                                ~@&   J@B   7@#  5@&&@@Y  &@7");
        System.out.println("P@P   G@J                                ~@&   J@B   7@# Y@B   #@J &@7");
        System.out.println("P@P   G@J                                ~@&   J@B   7@# ~@@Y7Y@@^ &@7");
        System.out.println("P@P   G@J                                ~@&   J@B   7@#  .?GBG?.  &@7");
        System.out.println("P@P   G@Y                                !@&   ?@G   7@#           &@7");
        System.out.println("P@P   ~&@&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&@@?    ^.   7@#...........&@7");
        System.out.println("P@P     :^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^:           7@@&&&&&&&&&&&@@7");
        System.out.println("P@P                                                  7@#  ....... .&@7");
        System.out.println("J@&!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!~P@&!!!!!!!!!~7@@~");
        System.out.println(" 7B&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&###&&&&&&&&&&&G~ ");
    }

    static void drawFirewall() {
        System.out.println("                 .       :~~~~~~~~~~~~~~~~~~~~~~^.^~~~~~~~~~~~~~~~~~~~~~~. ");
        System.out.println("               .Y7       !P55555555555555555555PJ.5555555555555555555555P^ ");
        System.out.println("             .JGG^       !5555555555555555555555J.55555555555555555555555^ ");
        System.out.println("            !GGJP:       !5555555555555555555555J.55555555555555555555555^ ");
        System.out.println("          ^5GP?7G~       !P555555555555555555555J.Y5555555555555555555555^ ");
        System.out.println("         JGGP7!7PP:.....:^!!!!!!!!!!~^~!!!!!!!!!~:~!!!!!!!!!!^^!!!!!!!!!!. ");
        System.out.println(" !!:.  :5GGG7!7!YGG55555555555555555?.Y5555555555555555555555^^5555555555^ ");
        System.out.println(" .PGPYJGGGGP7~!!7YGGP555555555555555J.Y5555555555555555555555^^5555555555^ ");
        System.out.println("  ^GG5J5GGGP7!~~!!JGGP55555555555555J.Y5555555555555555555555^^5555555555^ ");
        System.out.println("  .GGGY!?PGP7!~^~!!?PGGP55555555555PJ.Y555555555555555555555P^^P55555555P^ ");
        System.out.println("  .GGG5!!7Y5!7~^^~!!7PGGGJ!7?!!!!!!7~.!777777777!!!7777777777::7777777777: ");
        System.out.println("  :GGG?!!!!7!7~~^^~!!7PGGPY55JJJJJJJJYJJJJJJJJJY7.JJJJJJJJJJJYYJJJJJJJJJJ: ");
        System.out.println("  ~GGY!!!!!!7!~^^~^~7!YGGGGGP5555555555555555555J.5555555555555555555555P^ ");
        System.out.println("  7GG?!!!!!7!~::^~~~!!?GGGJPP5555555555555555555?.55555555555555555555555^ ");
        System.out.println("  ~GP7!!!!7!~:::^~~~~!7PP575GP55555555555555555PJ.5555555555555555555555P^ ");
        System.out.println("  .GG?!!!7!~::::::^~^~!77!!5P???????????????????!.7??????????????????????: ");
        System.out.println("   !BJ!!7!~^:::::::^~~!!!!!P57??????!.7?777777777?7777777777?:^??????????: ");
        System.out.println("    JP7!7!^:::::::::^~!7!!?P5555555PJ.Y555555555555555555555P^!P55555555P^ ");
        System.out.println("     757!!^::::::::::~!!!75555555555J.Y5555555555555555555555^!5555555555^ ");
        System.out.println("      :??7~:::::::::^!!7?5P555555555J.Y555555555555555555555P^!P555555555^ ");
        System.out.println("        :!!~:::::::^!7?JYYYYYYYYYYYY?.JYYYYYYYYYYYYYYYYYYYYYY^~YYYYYYYYYY^ ");
        System.out.println("           ......:::................. .......................  ..........  ");
    }

    static void drawLaptop() {
        System.out.println("                  :5J~.                      ");
        System.out.println("                  ?:^!?J7^.                  ");
        System.out.println("                  ?:?:^^~7??!:               ");
        System.out.println("                 .?~^  ..^^~!?J?~.           ");
        System.out.println("                 :!7.      .:^^~7?J7^.       ");
        System.out.println("                 !^J           .:^~!7J?!:    ");
        System.out.println("                 ?:7               .:^~!?J?^ ");
        System.out.println("                 J^~                  ..~~^B:");
        System.out.println("                .7!.                    ~^^G ");
        System.out.println("                !7J^                    ?.?5 ");
        System.out.println("            .:^^^^~!7!^.                J 5? ");
        System.out.println("        .:^^^~^?7?~~^^~77!^.            ?.G~ ");
        System.out.println("     .^~^..??J77!!77?!!^^^~7!~:.       :!:G: ");
        System.out.println(" .:^^:     .:^7!?!7~!!7!?~!^^~!!!^:.   ~:!P. ");
        System.out.println("!P!.          :^^!!!7~!~!~777^~^^~!!~^.J.JY  ");
        System.out.println(" :!??~:.   ^~^..^^^~^~!!7~~^~!?7?!^^^~!!.P?  ");
        System.out.println("    .^7?7^:~^:.   ..^^~^^~?!7!!!~~~!?..~YP~  ");
        System.out.println("        :~77!~^^^:..:~~   .^~?~~!^~^^!77~:   ");
        System.out.println("           .:!77~^^:          .^^~77!^.      ");
        System.out.println("               .^77!^.      .^!77^.          ");
        System.out.println("                   :~7?!::!77~:.             ");
        System.out.println("                      .^77^.                 ");
    }

    static void drawWallet() {
        System.out.println("                     .~J5PPPPPYJ?~           ");
        System.out.println("         :^~!~^:   ^GGJ77?JJJJ?77JGP^        ");
        System.out.println("     .?PP5YYYYYYPG#B^!PGJ~:..:~JGP~^BB.      ");
        System.out.println("    J&?!5P5J??5PP@?.BB:  ?JJJ7.  ^BB Y@.     ");
        System.out.println("   B#.P#~  .5: .@5 &P   ~@^..?@:   G& G&     ");
        System.out.println("  7@ G&   !@P@7:@.!&    ~@PYYB@.   .@^:@.    ");
        System.out.println("  5& @Y   Y#5#5:@:^@.   ~@^...&Y   :@:^@.    ");
        System.out.println(" .B@5&&555YPBPY5&#P&B555G@P555&#555#&5#&5^   ");
        System.out.println("?@~                                     ~@^  ");
        System.out.println("#B                                      .@~  ");
        System.out.println("#B                                      .@^  ");
        System.out.println("#B                             .^~~~~~~~7@5^.");
        System.out.println("#B                           ~BG?!77!!!!!!!@G");
        System.out.println("#B                          J@^ ^P555.     #G");
        System.out.println("#B                          @J  @Y  B@     &G");
        System.out.println("#B                          5&. ~G55G^     BG");
        System.out.println("#B                           !BP7~!!^^~^^^^@G");
        System.out.println("#B                             .~!!!777!J@P!:");
        System.out.println("#B                                      .@^  ");
        System.out.println("#B                                      .@~  ");
        System.out.println("J@^                                     ^@^  ");
        System.out.println(" ~PGGGGGGPGGGGGGGGPPGGGGGGGGGGGPGGGGGGGGG!   ");
    }

    static void winScreen() {
        System.out.println("                 ..::^^^^^^^^:..                  ");
        System.out.println("             .:^~~~^^^::::::^^~~~~^:.             ");
        System.out.println("          :^~~^:..              ..:^~~^.          ");
        System.out.println("       .^~~^:      ..::::::::..     .:^~~:.       ");
        System.out.println("     .^~~^.    .:^~~~~~~~~~~~~~~^:.    .^~~:      ");
        System.out.println("    :~~^.   .^~~~~~~^::~^::^~~~~~~~~^.   .^~~.    ");
        System.out.println("   ^~~:   .^~~~~~~~~.  ~:  ^~~~~~~~~~~^.   :~~:   ");
        System.out.println("  ^~~.   :~~~~~^..::   :.  .:^^~~~~~~~~~:   :~~:  ");
        System.out.println(" :~~:   ^~~~~~~:                :~~~~~~~~:   :~~. ");
        System.out.println(" ~~^   :~~~~~~~~~~.    ~~~^^     ^~~~~~~~~.   ~~^ ");
        System.out.println(":~~:   ~~~~~~~~~~~.    ~~~~^     ^~~~~~~~~^   :~~.");
        System.out.println("^~~.  .~~~~~~~~~~~.             :~~~~~~~~~~.  .~~:");
        System.out.println("^~~.  .~~~~~~~~~~~.    ::::..    .~~~~~~~~~.  .~~:");
        System.out.println(":~~:   ~~~~~~~~~~~.    ~~~~~~.    :~~~~~~~~   :~~.");
        System.out.println(" ~~^   :~~~~~~~^^^.    ^^^^^:     ^~~~~~~~:   ~~~ ");
        System.out.println(" :~~:   ^~~~~~~:                 :~~~~~~~:   :~~. ");
        System.out.println("  ^~~.   :~~~~~^...:   :.  .:::^~~~~~~~~:   :~~^  ");
        System.out.println("   ^~~:   .^~~~~~~~~.  ~:  ^~~~~~~~~~~^.   :~~^   ");
        System.out.println("    :~~^.   .^~~~~~~^::~^::^~~~~~~~~^.   .^~~:    ");
        System.out.println("     .^~~:.    .^~~~~~~~~~~~~~~~^^.    .^~~^.     ");
        System.out.println("       .^~~^.      ..::^^^^::..      .^~~^.       ");
        System.out.println("         .:^~~^:.                .:^~~^:          ");
        System.out.println("             .:^~~~^^::::::::^^~~~^:.             ");
        System.out.println("                 ..::^^^~~^^^::..                 ");
    }
}

class Room {
    String roomName;
    Room north, south, east, west;
    boolean visited; // Dijkstra, all nodes need to be visited
    int distance; // Dijkstra, find Distance to each room

    Room(String aRoomName) {
        roomName = aRoomName;
    }
}
