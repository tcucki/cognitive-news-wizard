# Cognitive Wizard Service

Contains services to register, process and provide feed data.

## Service Catalog

### /feedsourcegroup

- POST `/feedsourcegroup/create` argument `FeedGroupVO` returns `FeedGroupVO`  
Creates a Feed Source Group


- GET `/feedgroup/` returns `List<FeedGroupVO>`  
Lists all feed groups

### /feedsource

- POST `/feedsource/create` `CreateFeedSourceParamsVO` returns `FeedSourceVO`  
Creates a new feed entry for a group

### /rawfeedentry

- POST `/rawfeedentry/create` `RawFeedEntryVO` returns `RawFeedEntryVO` with no compacted content.  
Creates a new raw feed. Usually executed by *Cognitive Wizard Agent*. Saves content on compact form (zipped).
Process raw feed extracting relevant text saving on *feed_text* collection.
Skips feed registration whether finds other feed with same `feedEntryId`

- POST `/rawfeedentry/compactall` returns `ok` **  
Compacts contents for all feeds

- GET `/rawfeedentry/report/period?start&end` returns List<RawFeedReportItem>
Reports all raw feed entry for a given period. Dates the on form yyymmdd

### /feedprocessor

- POST `/feedprocessor/execute` returns `ok` **  
Process all raw feeds, extracting relevant text content, saving on *feed_text* collection.

### /propernoun

- GET `/propernoun/missing` returns `List<MissingProperNounVO>`  
Extracts a report with all proper nouns not registered on *proper_noun* collection

- GET `/propernoun/missing/report` `boolean lookInParagraphs`, `int countThreshold` returns `String`
CSV version of `/propernoun/missing` of the form  

| missing proper noun counter | proper noun | suggestion |
|-----------------------------|-------------|------------|
|999                          | name        | name       |


- POST `/propernoun/missing/registerbatch` `String` returns `String`  
Registers a batch of proper nouns of the form  

| counter | proper noun | actual proper noun | ignore |
|---------|-------------|--------------------|--------|
| 999     | name        | name               | X      |

For each line of the batch, adds a proper noun mapping to *proper_noun* collection, if *actual proper noun* field is not empty,
or adds a noun to be ignored if *ignore* is not empty. Does nothing otherwise.

### /mention

 - POST `/mention/extractall` returns `String`  
 Mines all feeds extracting known proper nouns mentions. Saves the result on `mention` collection, returning the count of total mentions.
 Clears previous mentions saved.



*Remarks*  
** methods for testing purposes only