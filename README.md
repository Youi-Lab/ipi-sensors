# ipi-sensors
Rest service for storing data from sensors made at Ipicyt

1. Register sensor into database:

curl -X POST "http://localhost:8083/sensky/ipisensors/greetsensorpar" -F "serial=IPI-SENSOR-00000100" -F "mac=00:00:00:01" -F "type=PM2.5" -F "units=um/m3"

or alternatively:
 
curl -X POST http://localhost:8083/sensky/ipisensors/greetsensorjson -H 'Content-type:application/json' -d '{"serial":"IPI-SENSOR-00000101", "mac":"00:00:00:01", "type":"PM2.5", "units":"um/m3"}'

2. Upload sensor´s data:

curl -X POST http://localhost:8083/sensky/ipisensors/uploadmeasurement -F "serial=IPI-SENSOR-00000101" -F "datetime=123456" -F "val=1000.1"

3. Retrieve data (json file):

curl -X POST http://localhost::8083/sensky/ipisensors/downloadjson -F "serial=IPI-SENSOR-00000101"
