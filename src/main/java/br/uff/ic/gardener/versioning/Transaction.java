package br.uff.ic.gardener.versioning;

/**
 *
 * @author Evaldo de Oliveira
 */
public class Transaction {

    private String message;
    private String date;
    private String tpTrans;
    public User user;

    public Transaction(String message, String date, String tpTrans)
    {
        this.setMessage(message);
        this.setDate(date);
        this.setTpTrans(tpTrans);
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return this.message;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return this.date;
    }

    public void setTpTrans(String tpTrans)
    {
        this.tpTrans = tpTrans;
    }

    public String getTpTrans()
    {
        return this.tpTrans;
    }

}
