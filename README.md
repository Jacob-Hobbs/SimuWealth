# 💲imuWealth

Check it out live here: (Currently undergoing security maintenance) 

_Note!_ The deployed live demonstration has been temporarily moved offline to implement more robust security features. Please enjoy the screenshots below!

**Username**: _admin@simuwealth.com_ 

**Password**: _password_

_...or make your own credentials!_

## Description

_SimuWealth_ stands as an inventive Spring Boot MVC project that demonstrates the harmonious integration of cutting-edge technologies in the domains of finance and education, with a unique feature allowing users to engage in paper trading for stocks. Operating seamlessly on AWS Elastic Beanstalk, this dynamic application harnesses the power of Java, Spring Boot, and JPA/Hibernate to construct a resilient and feature-rich platform. At its core lies a MySQL database, safeguarding data integrity and facilitating access, making it an ideal tool for both financial exploration and educational purposes, particularly in the realm of paper trading for stocks.

<img src="https://github.com/Jacob-Hobbs/SimuWealth/blob/master/src/main/resources/static/SimuWealth1.gif">

One of _SimuWealth_'s standout features is its seamless email delivery mechanism through Gmail SMTP. It excels in basic session management and user authentication, providing a secure and personalized experience. _SimuWealth_'s core functionality revolves around real-time and historical stock market data retrieved from the Alpha Vantage API. This empowers users to engage in simulated stock trading using virtual USD, with unique portfolio features tailored to each individual. Users can effortlessly buy and sell paper stocks using real-time data, curate watchlists, and generate insightful reports to monitor their positions effectively. The inclusion of stock time-series data empowers users to make informed financial decisions. Ultimately, _SimuWealth_ is not just a financial application; it is an educational and financial training tool, designed to enhance financial literacy and investment acumen.

## Architecture
SimuWealth, built using an MVC (Model-View-Controller) architecture, efficiently manages financial data by separating it into the Model, presents it to users through the View, and handles user interactions through the Controller, providing an organized structure for financial management and basic CRUD (Create, Read, Update, Delete) operations.

### Model-View-Controller Architecture
Spring MVC is a web application framework that follows the MVC architectural pattern. _SimuWealth_ effectively implements this pattern:
* **Model**: Within _SimuWealth_, the model component signifies the underlying data structure of essential entities such as Users, Stocks, and Reports. The application seamlessly integrates Hibernate and Java Persistence API (JPA) for efficient Object-Relational Mapping (ORM), facilitating interactions with the AWS-hosted MySQL database. These technologies ensure optimal data storage and retrieval processes.
* **View**: The view layer assumes responsibility for crafting an aesthetically pleasing and user-friendly interface within _SimuWealth_. Employing HTML, CSS, and Bootstrap, the application generates a visually engaging platform. This approach allows users to interact seamlessly with their stock portfolios, watchlists, and generated reports.
* **Controller**: Serving as vital intermediaries between the model and view, controllers in _SimuWealth_ harness the capabilities of Spring Boot's controller classes to manage incoming HTTP requests and route them to the corresponding services. These controllers oversee Create, Read, Update, and Delete (CRUD) operations, ensuring users can effortlessly manipulate various elements within their individual profiles.

### User, Stock, and Report CRUD Operations
* **Create**: _SimuWealth_ encompasses a robust Create functionality, empowering users to initiate various pivotal actions within the application. Firstly, users can seamlessly purchase stocks (thereby generating new stock objects within their portfolio) for realtime market prices. Moreover, _SimuWealth_ facilitates the creation of new user accounts, encompassing essential details such as first name, last name, email, and password. This feature ensures a personalized and secure experience for each user, fostering trust and accountability. Additionally, users can harness the application's capabilities to create timestamped reports, allowing them to meticulously manage their historical positions and overall performance. This feature is instrumental in tracking investment progress over time, facilitating data-driven decision-making. Lastly, SimuWealth empowers users to curate a watchlist of stocks that pique their interest, offering a tailored and convenient way to stay informed about potential investment opportunities.

  <img src="https://github.com/Jacob-Hobbs/SimuWealth/blob/master/src/main/resources/static/SimuWealth5.gif">

* **Read**: In _SimuWealth_, the Read aspect of the program offers users a wealth of critical information and insights. Users can access real-time stock information with ease, leveraging both time series and global quote data sourced from the Alpha Vantage API premium service. This feature empowers users to stay informed about current market conditions, enabling informed investment decisions. Furthermore, _SimuWealth_ allows users to draw upon their historical portfolio data, which is stored in the MySQL database, to generate comprehensive reports. This capability not only provides a snapshot of past performance but also aids users in analyzing their investment history, facilitating data-driven strategies for the future. Altogether, the Read functionality in _SimuWealth_ ensures that users have the essential tools to monitor their financial assets, make informed decisions, and gain valuable insights into their investment journey.

    <img src="https://github.com/Jacob-Hobbs/SimuWealth/blob/master/src/main/resources/static/SimuWealth2.gif">
  
* **Update**: The Update functionality in _SimuWealth_ offers users a dynamic platform to fine-tune and enhance their financial experience. Within their user profile, individuals can easily update key details, including their in-application profile picture, name, and email address, ensuring their information remains current and reflective of their preferences. SimuWealth takes security seriously by incorporating Multi-Factor Authentication (MFA) via email response, enabling users to securely update their passwords, bolstering account protection. Moreover, the application allows users to actively manage their unique portfolio by facilitating stock transactions, whether buying or selling, and updating their fund holdings. This feature ensures that users can adapt their investments in real-time, aligning with their evolving financial goals. Additionally, users can effortlessly maintain and adjust their watchlist cards and generated reports, tailoring their financial management to their specific needs. In summary, _SimuWealth_'s Update functionality empowers users with the flexibility and control to customize their financial journey effectively and securely.

     <img src="https://github.com/Jacob-Hobbs/SimuWealth/blob/master/src/main/resources/static/SimuWealth4.gif">

* **Delete**: The Delete functionality within SimuWealth provides users with a range of options to manage their financial data and account effectively. Users can initiate stock sales at real-time market prices, streamlining the process of adjusting their investment portfolio in response to changing market conditions. Additionally, SimuWealth allows users to declutter their simulation experience by removing watchlist cards and generated reports, ensuring that their account remains organized and relevant to their current objectives. The application takes account security seriously, and under the profile settings, users can opt to delete their account, complete with validation measures to prevent accidental or unauthorized deletion, ensuring that their data remains protected. Furthermore, users can efficiently remove investment funds from their account when needed, offering them the flexibility to fine-tune their financial holdings. SimuWealth's Delete functionality empowers users with the control and security to manage their financial information and account with confidence.

     <img src="https://github.com/Jacob-Hobbs/SimuWealth/blob/master/src/main/resources/static/SimuWealth3.gif">


### Email Handling and Session Management 
* **Password Reset Mechanism**: When a user initiates a password reset request in SimuWealth, a secure and automated process is set into motion. A notification email is promptly dispatched to the user's registered email address by the _SimuWealth_ system, bearing the sender attribution of the _SimuWealth_ team for clarity and trustworthiness. Within this email communication, a strategically crafted hyperlink is embedded, directing the user to a dedicated web page engineered explicitly for password reset procedures. This web page is equipped with a robust and user-friendly interface that facilitates the seamless creation of a new password. This multifaceted approach to password recovery not only ensures a streamlined user experience but also bolsters account security by employing email-based verification.

     <img src="https://github.com/Jacob-Hobbs/SimuWealth/blob/master/src/main/resources/static/SimuWealth6.gif">

* **Session Management and Security**: The user's details, including pertinent authentication data and preferences, are securely encapsulated within an active session. To fortify account security, _SimuWealth_ institutes a timeout mechanism, whereby sessions are automatically terminated following a defined period of user inactivity. This diligent session management approach serves as a safeguard against unauthorized access and potential security breaches, thereby upholding the integrity of user accounts and fostering a secure user experience throughout the application.

  <img src="https://github.com/Jacob-Hobbs/SimuWealth/blob/master/src/main/resources/static/SimuWealth7.gif">

## Getting Started

### Dependencies

* Web Browser (works well in Google Chrome)
* Java JDK 17.0.8 [(Download)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
* Spring Boot Starter Data JPA
* Spring Boot Starter Thymeleaf
* Spring Boot Starter Web
* Spring Boot Devtools
* MySQL Connector J
* Spring Boot Starter Test
* Spring Boot Maven Plugin
* Alpha Vantage Premium API Key [(Purchase)](https://www.alphavantage.co/premium/)

### Accessing the program

SimuWealth is hosted via AWS Elastic Beanstalk and can be accessed using the following link: (Currently undergoing security maintenance) 

## Help

* When I search for a stock, why does the daily performance chart not populate with time series data?
   - As explained by Alpha Vantage API, only certain stock symbols return historical time series data. You will notice that global quote information populates for every stock listed within Alpha Vantage's search API function, which can be used to make educational investments, however the absence of time series data for certain symbols is, admittedly,  a limitation of the technique used in this project. 

## Authors

Author can be contacted via the following link:

[Jacob Hobbs](https://www.linkedin.com/in/jacobrayhobbs/)

## Version History

* 0.1
    * Email link contained within reset email was set to local host endpoint. 
* 0.2
    * Email link corrected. 

## API Key Notes
Use of the premium Alpha Vantage API key is reserved for the author, and the author alone. Use outside of that facilitated by the SimuWealth interface is prohibited. The premium key required to run this is not included in code above and will need to be sourced individually. 

## Acknowledgments

* [Bootstrap Docs](https://getbootstrap.com/docs/5.0/getting-started/introduction/)
* [Spring Initializr](https://start.spring.io/)
* [Alpha Vantage API Documentation](https://www.alphavantage.co/documentation/)



