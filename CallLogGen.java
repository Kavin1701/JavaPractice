// input:
// 00:01:07,400-234-090 00:05:01,701-080-080 00:05:00,400-234-090

//Call log generator to print the bill
import java.util.*;

class PhoneEntry{
	private int hr,min,sec,totalSecs,billAmount;
	private long phoneNo;
	
	public PhoneEntry(int hr,int min,int sec,long phoneNo,int totalSecs,int billAmount){
		this.hr = hr;
		this.min = min;
		this.sec = sec;
		this.phoneNo = phoneNo;
		this.totalSecs = totalSecs;
		this.billAmount = billAmount;
	}
	
	public void setHr(int hr){
		this.hr = hr;
	}
	
	public void setMin(int min){
		this.min = min;
	}
	
	public void setSec(int sec){
		this.sec = sec;
	}
	
	public void setPhoneNo(long phoneNo){
		this.phoneNo = phoneNo;
	}
	
	public void setTotalSecs(int totalSecs){
		this.totalSecs = totalSecs;
	}
	
	public void setBillAmount(int billAmount){
		this.billAmount = billAmount;
	}
	
	public int getHr(){
		return hr;
	}
	
	public int getMin(){
		return min;
	}
	
	public int getSec(){
		return sec;
	}
	
	public long getPhoneNo(){
		return phoneNo;
	}
	
	public int getTotalSecs(){
		return totalSecs;
	}
	
	public int getBillAmount(){
		return billAmount;
	}
}

// driver class
public class CallLogGen{
	private static Scanner sc = new Scanner(System.in);
	
	// hashmap key=>phoneNum ,value=>phoneEntry
	private static HashMap<Long,PhoneEntry> hm = new HashMap<Long,PhoneEntry>();
	
	private static int max_call_duration;
	private static long max_phone_number;
	
	private static int billTotal;
	
	// getting entries
	private static String[] getInput(){
		String str;   // Temp for the entries
		System.out.print("\n Provide the entries : ");
		str = sc.nextLine();
		
		// to separate the data's
		return extractData(str.trim());
	}
	
	// to extract data's
	private static String[] extractData(String str){
		String data[] = str.split(" ");    // each entry in array form
		return data;
	}
	
	// to process the each entry
	private static void processData(String[] data){
		// to cal & store the data's of individual phoneNo
		for(String current : data){
			int hr = Integer.parseInt(current.substring(0,2)); //extract hr
			int min = Integer.parseInt(current.substring(3,5)); // extract min
			int sec = Integer.parseInt(current.substring(6,8)); // extract sec
			long phoneNo = Long.parseLong(current.substring(9).replaceAll("-",""));  //extract phoneNum
			
			// calculating duration in seconds
			int totalSecs = (hr*60*60) + (min*60) +sec;
			
			// calculating the billAmount
			int billAmount = 0;
			if(totalSecs < 300){  // 3 pounds per sec if < 5 mins
				billAmount = totalSecs * 3;
			}
			else{   // 150 pounds per min if >= 5 mins
				int totalMins = (hr*60) + min;
				if(sec > 0)  // min++ if sec >0
					totalMins++;
				billAmount = totalMins * 150;
			}
			
			// creating temp phoneEntry object
			PhoneEntry tempObj = new PhoneEntry(hr,min,sec,phoneNo,totalSecs,billAmount);
			
			if(hm.containsKey(phoneNo)){    // already present in hashmap
				PhoneEntry currentObj = (PhoneEntry)hm.get(phoneNo);
				int updatedBillAmount = currentObj.getBillAmount() + billAmount;
				int updatedTotalSecs = currentObj.getTotalSecs() + totalSecs;
				
				int[] updatedDuration = new int[3];
				updatedDuration = calUpdatedDuration(updatedTotalSecs);
				
				currentObj.setHr(updatedDuration[0]);
				currentObj.setMin(updatedDuration[1]);
				currentObj.setSec(updatedDuration[2]);
				
				currentObj.setTotalSecs(updatedTotalSecs);
				currentObj.setBillAmount(updatedBillAmount);
				
				hm.replace(phoneNo,currentObj);
			}
			else{    // storing temp phoneEntry if no entry was already for that number
				hm.put(phoneNo, tempObj);
			}
		
		}
		
		// to find the phoneNo with Max Call duration
		Iterator<Map.Entry<Long,PhoneEntry>> itr = hm.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<Long,PhoneEntry> element = itr.next();
			PhoneEntry t = (PhoneEntry) element.getValue();
		
			if(max_call_duration < t.getTotalSecs()){
				max_call_duration = t.getTotalSecs();
				max_phone_number = t.getPhoneNo();
			}
			
			// tie situation
			else if(max_call_duration == t.getTotalSecs()){
				max_phone_number = Math.min(t.getPhoneNo(),max_phone_number);
			}
		}
		
		// making free to max duration phone call
		PhoneEntry ent = hm.get(max_phone_number);
		ent.setBillAmount(0);
		
		
		// to find the Total BillAmount
		itr = hm.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<Long,PhoneEntry> element = itr.next();
			PhoneEntry t = (PhoneEntry) element.getValue();
			billTotal += t.getBillAmount();
		}
	}
	
	// to calculate updated call duration in hr,min,sec
	private static int[] calUpdatedDuration(int totalSecs){
		int updatedDuration[] = new int[3];
		updatedDuration[0] = totalSecs/(60*60);   // updated hr
		updatedDuration[1] = (totalSecs - (updatedDuration[0] * 60 * 60))/60;   // updated min
		updatedDuration[2] = totalSecs - (updatedDuration[0] * 60 * 60) - (updatedDuration[1] * 60);  // updated sec
		
		return updatedDuration;
	}
	
	// to display the final logs
	private static void display(){
		System.out.print("\n Phone Call Bill :\n");
		Iterator<Map.Entry<Long,PhoneEntry>> itr = hm.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry<Long,PhoneEntry> element = itr.next();
			PhoneEntry t = (PhoneEntry)element.getValue();
			
			System.out.println("\n   PhoneNo : "+t.getPhoneNo()+"\n"+
				"       Duration :: "+t.getHr()+" : "+t.getMin()+" : "+t.getSec()+"\n"+
				"      TotalSecs :: "+t.getTotalSecs()+"\n"+
				"     BillAmount :: "+t.getBillAmount());
		}
		System.out.println("   ---------------------\n   Total Bill : "+billTotal);
	}
			
		
	public static void main(String[] args){
		String[] data;
		
		data = getInput();
		processData(data);
		display();
		
		System.out.println("\n Success...");
	}
}