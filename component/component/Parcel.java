package component;

public class Parcel {
    private String mid;
    private String trackId;
    private String weight;
    private String serNo;
    private String recName;
    private String recAdd;
    private String recPin;
    private String senderName;
    private String senderId;

    public Parcel(String mid, String trackId, String weight, String serNo, String recName, String recAdd, String recPin, String senderName, String senderId) {
        this.mid = mid;
        this.trackId = trackId;
        this.weight = weight;
        this.serNo = serNo;
        this.recName = recName;
        this.recAdd = recAdd;
        this.recPin = recPin;
        this.senderName = senderName;
        this.senderId = senderId;
    }

    public Parcel(int int1, int int2, double double1, String string, int int3, String string2, String string3,
            String string4, String string5, int int4) {
        //TODO Auto-generated constructor stub
    }

    // Getters and Setters
    public String getMid() { return mid; }
    public void setMid(String mid) { this.mid = mid; }

    public String getTrackId() { return trackId; }
    public void setTrackId(String trackId) { this.trackId = trackId; }

    public String getWeight() { return weight; }
    public void setWeight(double d) { this.weight = Double.toString(d); }

    public String getSerNo() { return serNo; }
    public void setSerNo(String serNo) { this.serNo = serNo; }

    public String getRecName() { return recName; }
    public void setRecName(String recName) { this.recName = recName; }

    public String getRecAdd() { return recAdd; }
    public void setRecAdd(String recAdd) { this.recAdd = recAdd; }

    public String getRecPin() { return recPin; }
    public void setRecPin(String recPin) { this.recPin = recPin; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getSenderId() { return senderId; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
}
