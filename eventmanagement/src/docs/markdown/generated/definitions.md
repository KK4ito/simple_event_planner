## Definitions
### Event
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|attendees||false|Collection«User»||
|closingTime||false|string (date-time)||
|created||false|string (date-time)||
|description||false|string||
|endTime||false|string (date-time)||
|files||false|Collection«File»||
|id||false|integer (int64)||
|imageUri||false|string||
|name||false|string||
|speakers||false|Collection«User»||
|startTime||false|string (date-time)||
|updated||false|string (date-time)||


### File
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|contentType||false|string||
|created||false|string (date-time)||
|id||false|integer (int64)||
|name||false|string||
|updated||false|string (date-time)||
|uri||false|string||


### Link
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|href||false|string||
|rel||false|string||
|templated||false|boolean||


### Resources«Event»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|content||false|Collection«Event»||
|links||false|Link array||


### Resources«File»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|content||false|Collection«File»||
|links||false|Link array||


### Resources«Link»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|content||false|Collection«Link»||
|links||false|Link array||


### Resources«User»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|content||false|Collection«User»||
|links||false|Link array||


### Resource«Event»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|attendees||false|Collection«User»||
|closingTime||false|string (date-time)||
|created||false|string (date-time)||
|description||false|string||
|endTime||false|string (date-time)||
|files||false|Collection«File»||
|id||false|integer (int64)||
|imageUri||false|string||
|links||false|Link array||
|name||false|string||
|speakers||false|Collection«User»||
|startTime||false|string (date-time)||
|updated||false|string (date-time)||


### Resource«File»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|contentType||false|string||
|created||false|string (date-time)||
|id||false|integer (int64)||
|links||false|Link array||
|name||false|string||
|updated||false|string (date-time)||
|uri||false|string||


### Resource«User»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|email||false|string||
|firstName||false|string||
|id||false|integer (int64)||
|image||false|file||
|imageUri||false|string||
|internal||false|boolean||
|lastName||false|string||
|links||false|Link array||
|password||false|string||


### User
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|email||false|string||
|firstName||false|string||
|id||false|integer (int64)||
|image||false|file||
|imageUri||false|string||
|internal||false|boolean||
|lastName||false|string||
|password||false|string||


