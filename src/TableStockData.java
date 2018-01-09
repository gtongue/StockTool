import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by Gtongue on 3/26/2017.
 */
public class TableStockData {
    private SimpleStringProperty Symbol;
    private SimpleStringProperty Sector;
    private SimpleStringProperty Price;
    private SimpleStringProperty FiftyTwoHigh;
    private SimpleStringProperty DPS;
    private SimpleStringProperty FiftyTwoLow;
    private SimpleStringProperty MCap;
    private SimpleStringProperty EPS;
    private SimpleStringProperty PE;

    public TableStockData(String SYM)
    {
        Symbol = new SimpleStringProperty(SYM);
        Sector = new SimpleStringProperty("NO");
        Price = new SimpleStringProperty("NO");
        FiftyTwoHigh = new SimpleStringProperty("NO");
        FiftyTwoLow = new SimpleStringProperty("NO");
        DPS = new SimpleStringProperty("NO");
        MCap = new SimpleStringProperty("NO");
        EPS = new SimpleStringProperty("NO");
        PE = new SimpleStringProperty("NO");
    }

    public String getSymbol() {
        return Symbol.get();
    }

    public SimpleStringProperty symbolProperty() {
        return Symbol;
    }

    public void setSymbol(String symbol) {
        this.Symbol.set(symbol);
    }

    public String getSector() {
        return Sector.get();
    }

    public SimpleStringProperty sectorProperty() {
        return Sector;
    }

    public void setSector(String sector) {
        this.Sector.set(sector);
    }

    public String getPrice() {
        return Price.get();
    }

    public SimpleStringProperty priceProperty() {
        return Price;
    }

    public void setPrice(String price) {
        this.Price.set(price);
    }

    public String getFiftyTwoHigh() {
        return FiftyTwoHigh.get();
    }

    public SimpleStringProperty fiftyTwoHighProperty() {
        return FiftyTwoHigh;
    }

    public void setFiftyTwoHigh(String fiftyTwoHigh) {
        this.FiftyTwoHigh.set(fiftyTwoHigh);
    }

    public String getDPS() {
        return DPS.get();
    }

    public SimpleStringProperty DPSProperty() {
        return DPS;
    }

    public void setDPS(String DPS) {
        this.DPS.set(DPS);
    }

    public String getFiftyTwoLow() {
        return FiftyTwoLow.get();
    }

    public SimpleStringProperty fiftyTwoLowProperty() {
        return FiftyTwoLow;
    }

    public void setFiftyTwoLow(String fiftyTwoLow) {
        this.FiftyTwoLow.set(fiftyTwoLow);
    }

    public String getMCap() {
        return MCap.get();
    }

    public SimpleStringProperty MCapProperty() {
        return MCap;
    }

    public void setMCap(String MCap) {
        this.MCap.set(MCap);
    }

    public String getEPS() {
        return EPS.get();
    }

    public SimpleStringProperty EPSProperty() {
        return EPS;
    }

    public void setEPS(String EPS) {
        this.EPS.set(EPS);
    }

    public String getPE() {
        return PE.get();
    }

    public SimpleStringProperty PEProperty() {
        return PE;
    }

    public void setPE(String PE) {
        this.PE.set(PE);
    }
}
