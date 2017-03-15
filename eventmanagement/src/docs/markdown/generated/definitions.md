## Definitions
### Event
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|closingTime||false|string (date-time)||
|created||false|string (date-time)||
|description||false|string||
|endTime||false|string (date-time)||
|files||false|file array||
|id||false|integer (int64)||
|name||false|string||
|startTime||false|string (date-time)||
|updated||false|string (date-time)||


### File
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|created||false|string (date-time)||
|event||false|Event||
|id||false|integer (int64)||
|name||false|string||
|updated||false|string (date-time)||


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
|closingTime||false|string (date-time)||
|created||false|string (date-time)||
|description||false|string||
|endTime||false|string (date-time)||
|files||false|file array||
|id||false|integer (int64)||
|links||false|Link array||
|name||false|string||
|startTime||false|string (date-time)||
|updated||false|string (date-time)||


### Resource«File»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|created||false|string (date-time)||
|event||false|Event||
|id||false|integer (int64)||
|links||false|Link array||
|name||false|string||
|updated||false|string (date-time)||


### Resource«User»
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|email||false|string||
|firstName||false|string||
|id||false|integer (int64)||
|lastName||false|string||
|links||false|Link array||


### User
|Name|Description|Required|Schema|Default|
|----|----|----|----|----|
|email||false|string||
|firstName||false|string||
|id||false|integer (int64)||
|lastName||false|string||


