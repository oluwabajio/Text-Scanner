package phone.number.scanner.models;

public class BankAccountNo {

    private String name;
    private String bankAccountNo;

    public BankAccountNo(String name, String bankAccountNo) {
        this.name = name;
        this.bankAccountNo = bankAccountNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
        this.bankAccountNo = bankAccountNo;
    }
}
