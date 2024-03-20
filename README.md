# Road Buddy Application

Road Buddy  is a web application designed to facilitate ride-sharing between users. It allows users to either organize shared travels or join existing ones.

## Functional Requirements

### Entities
* Each user have a username, password, first name, last name, email, and phone number.
    Username must be unique and between 2 and 20 symbols.
    Password must be at least 8 symbols and include a capital letter, digit, and special symbol.
    First and last names must be between 2 and 20 symbols.
    Email must be valid and unique in the system.
    Phone number must be 10 digits and unique in the system.
* Each travel must have starting and ending points, departure time, number of free spots, and an optional comment.

### Public Part
* Accessible without authentication for anonymous users.
* Registration and login functionalities.
* Email verification for registration.
* Detailed information about Carpooling and its features.
* Lists of top travel organizers and top passengers.

### Private Part
* Accessible only to authenticated users.
* Profile management including updating profile and setting a profile photo.
* Creation of new travels.
* Browsing available trips, applying for trips as a passenger, and managing passenger requests.
* Feedback system for trips and users.

### Administrative Part
* Accessible to users with administrative privileges.
* User management including blocking/unblocking users.
* Viewing and managing travels.

## REST API
* Provides CRUD operations for users and travels.
* Allows blocking/unblocking users and searching users by username, email, or phone.
* Enables applying for a travel, managing applicants, and filtering/sorting travels.

## External Services
* Integration with Microsoft Bing Maps External Service for precise location and ride duration calculations.
* Cloudinary API for management and delivery of images and media content.

## Setting Up Swagger
To access the Swagger documentation for the REST API, follow the link below after running the application:
http://localhost:8080/swagger-ui/index.html
* Locate the "Authorize" Button: In the top right corner of the application interface, you will find an "Authorize" button.
* Enter Your Credentials: A login dialog box will appear. Enter your username and password in the respective fields.
* Access the Application: Once logged in successfully, you will have access to all the features and functionalities of the Carpooling application.

## Authors

  - **Aleksandrina Velikova**
  - **Ivona Stoyanova**
  - **Kaloyan Enchev**


