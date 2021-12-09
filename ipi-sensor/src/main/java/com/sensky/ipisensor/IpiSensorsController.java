package com.sensky.ipisensor;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.RestController;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import com.mongodb.MongoClientSettings;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import java.util. List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.mongodb.core.SimpleMongoClientDbFactory;

import org.springframework.data.mongodb.core.query.Criteria;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;
import org.springframework.data.mongodb.core.query.Query;
import static org.springframework.data.mongodb.core.query.Query.query;


@RestController
@RequestMapping("/sensky/ipisensors")
public class IpiSensorsController {

	static String senskyDb = "senskyIpiSensors";
	 
	@PostMapping("/greetsensorpar")
	public String greetsensorpar(@RequestParam String serial,
							     @RequestParam String mac,
							     @RequestParam String type,
							     @RequestParam String units) {

	    Log log = LogFactory.getLog(IpiSensorApplication.class);

       // Create mongo client
		MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), senskyDb) );

        // Check if sensor is already in database
		String json;
		Sensor sensor; 
		Sensor s;
        Query query = new Query(Criteria.where("serial").is(serial));
		json = "{" + "\"serial: \"" + serial + "\", mac: \"" + mac + "\", type: \"" + type +  "\", units: \"" + units + "}";
        sensor = mongoTemplate.findOne(query, Sensor.class);
		if (sensor == null) {
			s = mongoTemplate.insert(new Sensor(serial, mac, type, units));
			log.info( "YYYYYYYYYYYY" + s );
			return json;
		} else {
			return  "Sensor " + json +  " is already in the data base.";
		}
	} // curl -X POST "http://localhost:8080/sensky/ipisensors/greetsensorpar" -F "serial=IPI-SENSOR-00000100" -F "mac=00:00:00:01" -F "type=PM2.5" -F "units=um/m3"

	@PostMapping("/greetsensorjson")
	public String greetsensorjson( @RequestBody Sensor sensor) {

	    Log log = LogFactory.getLog(IpiSensorApplication.class);

       // Create mongo client
		MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), senskyDb) );

        // Check if sensor is already in database
		String json;
		Sensor s; 
        Query query = new Query(Criteria.where("serial").is(sensor.getSerial()));
		json = "{" + "\"serial: \"" + sensor.getSerial() + "\", mac: \"" + sensor.getMac() + "\", type: \"" + sensor.getType() +  "\", units: \"" + sensor.getUnits() + "}";
						log.info("$$$" + sensor.getSerial() + sensor.getMac()+ sensor.getType()+ sensor.getUnits()); 
        s = mongoTemplate.findOne(query, Sensor.class);
		if (s == null) {
			mongoTemplate.insert(new Sensor(sensor.getSerial(), sensor.getMac(), sensor.getType(), sensor.getUnits()));
			return json;
		} else {
			return  "Sensor " + json +  " is already in the data base.";
		}
	} // curl -X POST http://localhost:8080/sensky/ipisensors/greetsensorjson -H 'Content-type:application/json' -d '{"serial":"IPI-SENSOR-00000101", "mac":"00:00:00:01", "type":"PM2.5", "units":"um/m3"}'

	@PostMapping("/uploadmeasurement")
	public String datasensor( @RequestParam String serial, 
	                          @RequestParam String datetime, 
							  @RequestParam String val) {

		Log log = LogFactory.getLog(IpiSensorApplication.class);

       // Create mongo client
		MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), senskyDb) );

        // Check if sensor is already in database
		String json;
		Sensor sensor; 
        Query query = new Query(Criteria.where("serial").is(serial));
		json = "{" + "\"serial: \"" + serial + "\", datetime: \"" + datetime + "\", val: \"" + val + "}";
        sensor = mongoTemplate.findOne(query, Sensor.class);
		if (sensor != null) {
			sensor.updateData(datetime, val);
			mongoTemplate.updateFirst(query(where("id").is(sensor.getId())), update("data", sensor.getData()), Sensor.class);
			return json;
		} else {
			return  "Error: Sensor " + json +  " is not in the data base.";
		}
	} // curl -X POST http://localhost:8080/sensky/ipisensors/uploadmeasurement -F "serial=IPI-SENSOR-00000101" -F "datetime=123456" -F "val=1000.1" 
	
	@PostMapping("/downloadjson")
	public String datasensor( @RequestParam String serial ) {

		Log log = LogFactory.getLog(IpiSensorApplication.class);

       // Create mongo client
		MongoTemplate mongoTemplate = new MongoTemplate(new SimpleMongoClientDbFactory(MongoClients.create(), "senskyIpiSensors") );

        // Check if sensor is already in database
		String json;
		Sensor sensor; 
        Query query = new Query(Criteria.where("serial").is(serial));
		json = "{" + "\"serial: \"" + serial + "}";
        sensor = mongoTemplate.findOne(query, Sensor.class);
		if (sensor != null) {
			return sensor.toJson();
		} else {
			return  "Error: Sensor " + json +  " is not in data base.";
		}
	} // curl -X POST http://localhost:8080/sensky/ipisensors/downloadjson -F "serial=IPI-SENSOR-00000101"  

}

