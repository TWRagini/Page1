package com.example.page1;

public class InvoiceModel {
    //String SrNo,SerialNumber,Model,InvoiceNumber;
    String SrNo, Product, SubCat, Model,SerialNumber,Incentive,act;


    public InvoiceModel(String SrNo, String Product,String SubCat,String Model,String SerialNumber, String Incentive, String act)
    {
        // srNo+","+productCat+","+model+","+subCat+","+incentiveAmt+","+serialNo+","+actn ;
        this.SrNo=SrNo;
        this.Product=Product;
        this.SubCat = SubCat;
        this.Model=Model;
        this.SerialNumber=SerialNumber;
        this.Incentive = Incentive;
        this.act = act;
    }


    public String getSrNo() {
        return SrNo;
    }

    public String getProduct() {
        return Product;
    }

    public String getSubCat() {
        return SubCat;
    }

    public String getModel() {
        return Model;
    }

    public String getSerialNumber() {
        return SerialNumber;
    }

    public String getIncentive() {
        return Incentive;
    }

    public String getAct() {
        return act;
    }
//    public String getSrNo() {return SrNo; }
//    public String getSerialnumber(){return SerialNumber;}
//    public String getModel(){return Model;}
//    public String getInvoiceNumber(){ return InvoiceNumber;}
}
