package com.example.page1;

public class IncentiveModel {
    String SrNo,SerialNumber,Model,InvoiceNumber,SaleDate,Incentive,Status;
    public IncentiveModel(String SrNo,String SerialNumber,String Model,String InvoiceNumber,String SaleDate,String Incentive,String Status)
    {
        this.SrNo=SrNo;
        this.SerialNumber=SerialNumber;
        this.Model=Model;
       this.InvoiceNumber=InvoiceNumber;
        this.SaleDate=SaleDate;
        this.Incentive=Incentive;
        this.Status=Status;
    }
    public String getSrNo() {
        return SrNo;
    }
    public String getSerialnumber(){return SerialNumber;}
    public String getModel(){return Model;}
    public String getIncentive(){return Incentive;}
    public String getSaleDate(){return SaleDate;}
    public String getStatus(){return Status;}
    public String getInvoiceNumber(){ return InvoiceNumber;}

}
