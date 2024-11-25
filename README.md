Document management system (DMS) is designed to allow the organization to manage company documentation and its publication to employees.

It includes following functions:<br>
1 As ADMIN:<br>
1.1 Log in to DMS as ADMIN <br>
1.2 To create accounts with role EMPLOYEE or ADMIN <br>
1.3 To edit accounts details<br>
1.4 To delete accounts<br>
1.5 Upload new document into DMS<br>
1.6 Edit document details in DMS<br>
1.7 Add new version of existing documents in DMS<br>
1.8 To see audit log of account creation/update/delete<br>
1.9 To see audit log of document upload/update/delete<br>
1.9 Make publication (notify the employees in DMS) of new document or new revision of existing document <br>
1.10 Trace the progress of employees read/acknowledgement with new document or revision of existing document.<br>
1.11 To see the list of all documents<br>
1.12 To apply filters to the list of documents<br>
1.13 Download the list of all documents/users<br>
1.14 Trace the next revision date of documents and get notification in DMS about expired documents<br>
1.15 Share documents to persons outside of company for certain period<br>

<br>
2 As EMPLOYEE:<br>
2.1 Log in to DMS as EMPLOYEE<br>
2.2 To change account's password<br>
2.3 Search for required documents<br>
2.4 To see the list of all documents<br>
2.5 To apply filters to the list of documents<br>
2.6 Get notified about new document or new revision of existing document in DMS<br>
2.7 To download documents from DMS<br>
2.8 Get acknowledge with new document or new revision of existing document<br>
2.9 Make comments on documents<br>
<br>
3 As GUEST:<br>
3.1 Open and download documents to which he received direct link<br>

<br>
Functions/improvements to be introduced into next version of DMS:<br>
- Improvement of user interface; <br>
- Add feature to change user interface to other languages;<br>
- Sharing link to the document with external users; <br>
- Forgot password function for users using email;<br>
- Email notifications to user about account creation;<br>
- Email notification to user about request for validation of document;<br>
- Email notification to user about new revision of document;<br>
- Sorting/filtering button on "Documents browse" and "Users browse" page;<br>
- Administration page for adding new categories (Role, Document types, Document statuses);<br>
- Function for filtering list of documents/user before export;<br>
- Function for adding digital signature to document;<br>
- Function for exporting list of document/user transactions [UPDATE: Implemented];<br>
- Function for reply to comments added to document;<br>
- User account logout after certain period of inactivity;<br>
- REST API endpoints page for third party integration;<br>
- Add deadline for document validation by responsible person;<br>
- Handling incorrect input from user / input validation;<br>
- Add tutorial page on basic functions of DMS (maybe video);<br>
- Implement page pagination;<br>
- Add restriction to revision validation: revision author cannot validate its own revision request [UPDATE: Implemented];<br>
- Add exception handling in service layer;<br>
- Add restriction to users to change other users password [UPDATE: Implemented];<br>
- Record the logger data into the file;<br>
- Make document code to be created automatically depending of document type and the next available number (POL-002, POL-003 and etc) [UPDATE: Implemented];<br>
- More test coverage (unit and integration);<br>
- Correct tests name convention;<br>
- Use DTO for all operations in controller (POST and GET);<br>
<br>
Full presentation of project:<br>
[Final project Presentation.drawio.pdf](https://github.com/user-attachments/files/17706020/Final.project.Presentation.drawio.pdf)<br>

