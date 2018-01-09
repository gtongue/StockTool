import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.util.ArrayList;
import java.util.List;

public class Controller {

    public TableView<TableStockData> Table;
    public TableColumn SYM;
    public TextField SYMLabel;
    public ArrayList<TableColumn> Columns;
    ObservableList<TableStockData> data;

    public Controller(){

    }

    @FXML
    public void initialize()
    {
        Columns = new ArrayList<>();
        SYM  = new TableColumn();
        List<TableStockData> list = new ArrayList<>();
        list.add(new TableStockData("AAPL"));

        SYM = new TableColumn("SYM");
        SYM.setCellValueFactory(new PropertyValueFactory<String, String>("Symbol"));
        SYM.setCellFactory(TextFieldTableCell.forTableColumn());
        SYM.setEditable(false);

        Columns.add(SYM);

        data = FXCollections.observableList(list);
        Table.setItems(data);
        Table.setEditable(false);

        UpdateColumns();
    }

    public void addPrice()
    {
        addNewColumn("Price");
    }
    public void addMarketCap()
    {
        addNewColumn("MCap");
    }
    public void addSector()
    {
        addNewColumn("Sector");
    }
    public void addFiftyTwoHigh()
    {
        addNewColumn("FiftyTwoHigh");
    }
    public void addFiftyTwoLow()
    {
        addNewColumn("FiftyTwoLow");
    }
    public void addDPS()
    {
        addNewColumn("DPS");
    }
    public void addEPS()
    {
        addNewColumn("EPS");
    }
    public void addPE()
    {
        addNewColumn("PE");
    }
    public void addNewColumn(String s)
    {
        TableColumn MarketCap = new TableColumn(s);
        MarketCap.setCellValueFactory(new PropertyValueFactory<String, String>(s));
        MarketCap.setCellFactory(TextFieldTableCell.forTableColumn());
        MarketCap.setEditable(false);
        Columns.add(MarketCap);
        UpdateColumns();
        UpdateData();
    }
    public void addSymbol()
    {
        String Symbols = SYMLabel.getText();
        if(Symbols.equals("")) return;
        String[] SymbolsArray = Symbols.split(",");
        for(String Symbol: SymbolsArray)
        {
            Symbol = Symbol.replace(" ", "");
            SYMLabel.setText("");
            data.add(new TableStockData(Symbol));
        }
        int row = data.size() - 1;
        Table.requestFocus();
        Table.getSelectionModel().select(row);
        Table.getFocusModel().focus(row);
        UpdateData();
    }

    public void onButtonClick()
    {
        TableStockData book = new TableStockData("MSFT");
        data.add(book);
        int row = data.size() - 1;
        // Select the new row
        Table.requestFocus();
        Table.getSelectionModel().select(row);
        Table.getFocusModel().focus(row);
        UpdateData();
    }
    public void UpdateColumns()
    {
        Table.getColumns().removeAll();
        Table.getColumns().clear();
        for(TableColumn col : Columns)
        {
            Table.getColumns().add(col);
        }
    }
    public void UpdateData()
    {
        ArrayList<String> currentCollumn = new ArrayList<>();
        for(TableColumn column : Table.getColumns())
        {
            currentCollumn.add(((PropertyValueFactory)column.cellValueFactoryProperty().getValue()).getProperty());
        }
        for(TableStockData d : data)
        {
            if(currentCollumn.contains("Price"))
                d.setPrice("0");
            if(currentCollumn.contains("MCap"))
                d.setMCap("0");
            if(currentCollumn.contains("Sector"))
                d.setSector("Sector");
            if(currentCollumn.contains("FiftyTwoHigh"))
                d.setFiftyTwoHigh("0");
            if(currentCollumn.contains("FiftyTwoLow"))
                d.setFiftyTwoLow("0");
            if(currentCollumn.contains("DPS"))
                d.setDPS("0");
            if(currentCollumn.contains("EPS"))
                d.setEPS("0");
            if(currentCollumn.contains("PE") && currentCollumn.get(currentCollumn.indexOf("PE")).equals("NO"))
                d.setPE("0");
        }
    }
    public void GenertateFile()
    {
        String Header = "";
        ArrayList<String> Columns = new ArrayList<>();
        for(TableColumn column : Table.getColumns()) {
            Header += ((PropertyValueFactory) column.cellValueFactoryProperty().getValue()).getProperty() + ",";
            Columns.add(((PropertyValueFactory) column.cellValueFactoryProperty().getValue()).getProperty());
        }

        ArrayList<Stock> stocks = new ArrayList<>();
        for(TableStockData d : data)
        {
            stocks.add(new Stock(d.getSymbol()));
        }
        Data outputData = new Data();
        outputData.Header = Header;
        LiveData.GetStockDailyInfo(stocks);
        int counter = 0;
        for(Stock s : stocks){
            TableStockData d = data.get(counter);
            for(String col : Columns){
                switch (col){
                    case "Symbol":
                        outputData.addNewDataPoint(s.SYM);
                        break;
                    case "Price":
                        outputData.addNewDataPoint(s.Price);
                        d.setPrice(""+s.Price);
                        break;
                    case "MCap":
                        outputData.addNewDataPoint(s.MCap);
                        d.setMCap(""+s.MCap);
                        break;
                    case "Sector":
                        outputData.addNewDataPoint(s.Sector);
                        d.setSector(""+s.Sector);
                        break;
                    case "FiftyTwoHigh":
                        outputData.addNewDataPoint(s.FiftyTwoHigh);
                        d.setFiftyTwoHigh(""+s.FiftyTwoHigh);
                        break;
                    case "FiftyTwoLow":
                        outputData.addNewDataPoint(s.FiftyTwoLow);
                        d.setFiftyTwoLow(""+s.FiftyTwoLow);
                        break;
                    case "DPS":
                        outputData.addNewDataPoint(s.DPS);
                        d.setDPS(""+s.DPS);
                        break;
                    case "EPS":
                        outputData.addNewDataPoint(s.EPS);
                        d.setEPS(""+s.EPS);
                        break;
                    case "PE":
                        outputData.addNewDataPoint(s.PE);
                        d.setPE(""+s.PE);
                        break;
                    default:
                        System.out.println("Unexpected column");
                        break;
                }
            }
            counter++;
            outputData.newSYM();
        }
        UpdateColumns();
        outputData.SaveData("Data");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Done");
        alert.setHeaderText("Finished Generating File.");
        alert.showAndWait();
    }
}