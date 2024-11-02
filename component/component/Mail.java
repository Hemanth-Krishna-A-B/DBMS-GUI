package component;

public class Mail {
    private int mid;
    private int trackId;
    private double weight;
    private String serNo;
    private int cid;
    private String recName;
    private String recAdd;
    private String recPin;
    private String senderName;
    private int senderId;

    public Mail(int mid, int trackId, double weight, String serNo, int cid, 
                String recName, String recAdd, String recPin, 
                String senderName, int senderId) {
        this.mid = mid;
        this.trackId = trackId;
        this.weight = weight;
        this.serNo = serNo;
        this.cid = cid;
        this.recName = recName;
        this.recAdd = recAdd;
        this.recPin = recPin;
        this.senderName = senderName;
        this.senderId = senderId;
    }

    public int getMid() { return mid; }
    public int getTrackId() { return trackId; }
    public double getWeight() { return weight; }
    public String getSerNo() { return serNo; }
    public int getCid() { return cid; }
    public String getRecName() { return recName; }
    public String getRecAdd() { return recAdd; }
    public String getRecPin() { return recPin; }
    public String getSenderName() { return senderName; }
    public int getSenderId() { return senderId; }
    
    public void setWeight(double weight) { this.weight = weight; }
    public void setSerNo(String serNo) { this.serNo = serNo; }
    public void setRecName(String recName) { this.recName = recName; }
    public void setRecAdd(String recAdd) { this.recAdd = recAdd; }
    public void setRecPin(String recPin) { this.recPin = recPin; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
}
