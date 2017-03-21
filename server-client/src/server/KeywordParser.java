package server;

/**
 * Created by m on 3/20/17.
 */
public class KeywordParser {
    private String keyword;
    private String argument;

    public KeywordParser(String keyword, String argument){
        this.keyword = keyword;
        this.argument = argument;
    }

    public void comparKeyword(){
        switch (keyword){
            case "uname" :
                UnameParser name = new UnameParser(argument);
                break;

            case "cpong" :
                CpingParser ping = new CpingParser();
                ping.pingConfermation();
                break;

            case "cname" :
                CnameParser namechange = new CnameParser(argument);
                break;

            case "chatm" :
                ChatmParser chat = new ChatmParser(argument);
                break;

            case "cquit" :
                CquitParser quit = new CquitParser(argument);
                break;

            default:
                System.out.println("the enterd keyword is not a vaild input");
                break;
        }
    }
}
