import java.io.PrintWriter;
import java.util.ArrayList;

/*
class DataPoint {
	public DataPoint(){
		SYM = Name = Sector = "";
		Price = EPS = PE = DPS = FiftyTwoLow = FiftyTwoHigh = MCap = priceYearAgo = priceSixMonthsAgo = priceThreeMonthsAgo = priceOneMonthAgo = 0;
	}
	String SYM, Name, Sector;
	double Price;
	double EPS,PE,DPS;
	double FiftyTwoLow, FiftyTwoHigh;
	double MCap;
	double priceYearAgo;
	double priceSixMonthsAgo;
	double priceThreeMonthsAgo;
	double priceOneMonthAgo;
}*/
class DataPoint {
    ArrayList<String> memes;
    public DataPoint(DataPoint currentPoint) {
        memes = new ArrayList<String>(currentPoint.memes);
    }
    public DataPoint(){
        memes = new ArrayList<String>();
    }
}

public class Data {
    String Header = "SYM,Name,Sector,Price,EPS,P/E,DPS,52WeekL,52WeekH,MCap,P+DPS,(P+DPS)/E,p1yr,p6mo,p3mo,p1mo";
    private DataPoint currentPoint = new DataPoint();
    private ArrayList<DataPoint> memes = new ArrayList<>();

    public Data(){}

    public Data(String Header){
        this.Header = Header;
    }

    public void addNewDataPoint(String val){
        if(val.isEmpty()){
            currentPoint.memes.add("X");
        }else{
            currentPoint.memes.add(val);
        }
    }
    public void addNewDataPoint(Double val){
        if(val == 0)
            currentPoint.memes.add("X");
        else
            currentPoint.memes.add(val+ "");
    }

    public void newSYM(){
        memes.add(new DataPoint(currentPoint));
        currentPoint.memes.clear();
    }

    public void SaveData(String filename){
        PrintWriter pw = Utils.MakeNewFile("output_files/" + filename + ".csv");
        pw.write(Header + "\n");
        for(DataPoint meme : memes){
            for(String s : meme.memes){
                pw.write(s+",");
            }
            pw.write("\n");
        }
        pw.close();
    }
}