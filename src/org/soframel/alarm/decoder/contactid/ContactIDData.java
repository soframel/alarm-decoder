package org.soframel.alarm.decoder.contactid;

/**
 * User: sophie
 * Date: 2/3/14
 */
public class ContactIDData {

    //data in message
    private int ownerID;
    private int syntax;
    private int eventKind;
    private int eventCode;
    private int userID;
    private int zone;
    private String checksum;

    //decoded data
    private String eventName;
    private String groupName;
    private String zoneName;

    public ContactIDData(){

    }
    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }




    public int getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(int ownerID) {
        this.ownerID = ownerID;
    }

    public int getSyntax() {
        return syntax;
    }

    public void setSyntax(int syntax) {
        this.syntax = syntax;
    }

    public int getEventKind() {
        return eventKind;
    }

    public void setEventKind(int eventKind) {
        this.eventKind = eventKind;
    }

    public int getEventCode() {
        return eventCode;
    }

    public void setEventCode(int eventCode) {
        this.eventCode = eventCode;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Override
    public String toString() {
        return  groupName+": "+eventKind+" "+eventName+". zone="+zoneName+", Partition="+ userID +", owner="+ownerID;
    }

    public static ContactIDData parseMessage(String data) throws ContactIDException{
        ContactIDData c=new ContactIDData();
        if(data.length()!=16){
            throw new ContactIDException("Illegal number of characters in contact ID: "+data.length());
        }
        else{
            String ownerS=data.substring(0, 4);
            int owner=-1;
            try{
                owner=Integer.parseInt(ownerS);
            }catch(NumberFormatException e){
                throw new ContactIDException("Could not parse owner string: "+ownerS);
            }
            c.setOwnerID(owner);

            String syntaxS=data.substring(4, 6);
            int syntax=-1;
            try{
                syntax=Integer.parseInt(syntaxS);
            }catch(NumberFormatException e){
                throw new ContactIDException("Could not parse syntax string: "+syntaxS);
            }
            c.setSyntax(syntax);

            String eventKindS=data.substring(6, 7);
            int eventKind=-1;
            try{
                eventKind=Integer.parseInt(eventKindS);
            }catch(NumberFormatException e){
                throw new ContactIDException("Could not parse eventKind string: "+eventKindS);
            }
            c.setEventKind(eventKind);

            String eventCodeS=data.substring(7, 10);
            int eventCode=-1;
            try{
                eventCode=Integer.parseInt(eventCodeS);
            }catch(NumberFormatException e){
                throw new ContactIDException("Could not parse eventCode string: "+eventCodeS);
            }
            c.setEventCode(eventCode);

            String userS=data.substring(10, 12);
            int user=-1;
            try{
                user=Integer.parseInt(userS);
            }catch(NumberFormatException e){
                throw new ContactIDException("Could not parse userID string: "+userS);
            }
            c.setUserID(user);

            String zoneS=data.substring(12, 15);
            int zone=-1;
            try{
                zone=Integer.parseInt(zoneS);
            }catch(NumberFormatException e){
                throw new ContactIDException("Could not parse zone string: "+zoneS);
            }
            c.setZone(zone);

            String checksum=data.substring(15, 16);
            c.setChecksum(checksum);
        }
        return c;
    }
}
