package com.sensky.ipisensor;

import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;
import java.lang.Double;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class Sensor {
   
    public String id;
	public String serial;
	public String mac;
	public String type; 
	public String units; 
	public String token;
	public List<SensorData> data; 
	  
	public Sensor(String serial, String mac, String type, String units) {
        
		// id is automatically assigned during init (mongo=)
		this.serial = serial;
		this.mac = mac;
		this.type = type;
		this.units = units;
		this.token = "";
		this.data = new ArrayList<SensorData>();
	}

	public String getId() {
		return id;
	}

	public String getSerial() {
		return serial;
	}	

	public String getMac() {
		return mac;
	}

	public String getType() {
		return type;
	}

	public String getUnits() {
		return units;
	}
	
	public List<SensorData> getData(){
		return data;
	}

	public void setId(String i) {
		id = i;
	}
	public void setSerial(String s) {
		serial = s;
	}

	public void setMac(String m) {
		mac = m;
	}

    public void setType(String t) {
		type = t;
	}

    public void setUnits(String u) {
		units = u;
	}

    public  boolean isInteger(String string) {
        int intValue;
		boolean ret;
		
        if(string == null || string.equals("")) {
            ret = false;
        }  
        try {
            intValue = Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
           ret = false;
        }
		return ret;	
    }

    public boolean isDouble(String string) {
        double doubleValue;
		boolean ret;
		
        if(string == null || string.equals("")) {
            ret =false;
        }   
        try {
            doubleValue = Double.parseDouble(string);
            return true;
        } catch (NumberFormatException e) {
          ret = false;
        }
		return ret;
    }

	public boolean updateData(String datetime, String val) {

	    Log log = LogFactory.getLog(IpiSensorApplication.class);

        boolean ret = false;
		SensorData sd = new SensorData(datetime, val);

       if ( isInteger(datetime)  &&  isDouble(val) ) { 
			data.add(sd);
			ret = true;
		} else {
			ret = false;
		}
		return ret;
	}

	public String toJson() {

		Log log = LogFactory.getLog(IpiSensorApplication.class);
		String json = "{\"serial\": "  + "\"" + serial  + "\"" + ", \"mac\": " + "\"" + mac +  "\""  + ", \"type\": " + "\"" + type + "\"" + ", \"units\": " + "\"" + units  + "\"" 
		                               +   " ,\"data\": "  +"[";

		for(int i = 0; i < data.size(); i++){
			if ( i < data.size() - 1) {
			    json +=  "{\"datetime\": " + "\"" + data.get(i).getDatetime()  + "\"" +  ", \"val\": " +  "\"" + data.get(i).getVal() +  "\""  + "},";
			} else {
				json +=  "{\"datetime\": " + "\"" + data.get(i).getDatetime() + "\"" + ", \"val\": " + "\"" + data.get(i).getVal() +  "\""  + "}";
			}
		}
		json = json + "]}";
		return json;
	}

    @Override
    public String toString() {
      return "Sensor [id=" + id + ", serial=" + serial + ", mac=" + mac + ", type=" + type + ", units=" + units + "]";
    }

}