# COGEclipseClient
This repository hosts the implementation of the COGEclipseClient and its various components. 
The CollabHubClient codebase consists of the implementation of the following:

•	Apache HttpComponents Client Component: This component establishes a connection with the COG server once the collaborator starts collaborating over a selected project (StartCollaborationClient.java).

•	ActivityMetaData Provider Component: This component (StartCollaborationHandler.java) creates the background UI thread for capturing developer activities within the IDE. It obtains activity metadata information and current file content and sends it to the COG server with the help of the HttpClient component (CollabUserActivityClient.java).

•	IDE DecorationContributor Plugin Component: Invokes the collaboratorInboxDecorator plug-in bundled in the DecorationPlugin repository. Also contains the code for collaboratorsInfoViewParts plug-in (DirectCollaboratorsViewPart.java, IndirectCollaboratorsViewPart.java, OpenIndirectCollaboratorsViewPart.java and SemiDirectCollaboratorsViewPart.java) and collaboratorsConflictsViewParts plug-in (ConflictMessagesView.java).

