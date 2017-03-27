## Definitions
### Event
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|attendees||false|Collection«EventAttendee»||
|attendeesCount||false|integer (int32)||
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


### EventAttendee
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|event||false|Event||
|foodType||false|enum (VEGI, NORMAL, NONE)||
|id||false|integer (int64)||
|user||false|User||


### File
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|contentType||false|string||
|created||false|string (date-time)||
|id||false|integer (int64)||
|name||false|string||
|updated||false|string (date-time)||
|uri||false|string||


### HttpEntity
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|body||false|object||


### Link
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|href||false|string||
|rel||false|string||
|templated||false|boolean||


### RepositorySearchesResource
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|links||false|Link array||


### Resources«EventAttendee»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|content||false|Collection«EventAttendee»||
|links||false|Link array||


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


### Resource«EventAttendee»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|event||false|Event||
|foodType||false|enum (VEGI, NORMAL, NONE)||
|id||false|integer (int64)||
|links||false|Link array||
|user||false|User||


### Resource«Event»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|attendees||false|Collection«EventAttendee»||
|attendeesCount||false|integer (int32)||
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


