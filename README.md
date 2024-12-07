HeartLink ❤️ - A Platform for Community-Driven Donations

1.Problem Statement 📜

In many communities, people have items they no longer need that could greatly benefit others. However, there is often a lack of a streamlined, user-friendly platform to connect those willing to donate with individuals in need. Current solutions are limited to specific categories and lack essential security and communication features, which are necessary for ensuring a safe, efficient, and trustworthy donation experience.

HeartLink seeks to address these gaps by providing a web-based application where individuals with items to donate (Donors) can seamlessly connect with those who need them (Receivers). This application is designed to encourage a spirit of community and compassion, offering an intuitive and secure environment for sharing items that are no longer needed.

Through HeartLink, users are empowered to reduce waste, embrace minimalism, and contribute to sustainability. By facilitating connections within communities, the platform strengthens community ties, a culture of sharing and fulfillment that makes giving and receiving items simple and rewarding.

2.Requirement Analysis 👩‍💻

&nbsp;&nbsp;&nbsp;&nbsp;There are 2 types of users: ADMIN, MEMBER.

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ADMIN : set up item categories

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;MEMBER: 

  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;•	As a donar, can create item, update ,find items, select a receiver to give the claimed item, send/receive messages.

  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;•	As a receiver, can claim the item from a donar, send/receive messages.


3.Architecture 💡

&nbsp;&nbsp;&nbsp;&nbsp;Technologies Used:

  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Back-end:
    
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;•	SpringBoot, Spring MVC, Spring Data JPA, Spring Security,AWS SDK
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;•	Database: Postgres
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;•	Authentication and Authorization: JWT token
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;•	Testing: JUnit, Mockito, Postman

CI/CD

  &nbsp;&nbsp;&nbsp;&nbsp;•	GitHub Action for Back-end and its source code is automatically built

<img width="1000" alt="azureDeployment-v2" src="https://github.com/user-attachments/assets/be55ff0b-3027-4aed-90f5-d324f0b732a0">

Class Diagram

![classDiagram_v3](https://github.com/user-attachments/assets/a304ba9a-5cf2-4ed0-a6a2-7820f39ff432)


ER Diagram

<img width="1000" alt="architecture-v1" src="https://github.com/user-attachments/assets/4dfd4fb1-884d-4c00-92f3-ff0db315e53d">

Local installation
  1.	Set up database connection in application.properties
  2.	And then run application

4. Deployment in Azure 🚀

  (1)	Build Jar file ( Please comment on local DB connection)

     gradle clean build

   (2) Create jar file, (e.g heartlink-1.0.1.jar)  

     gradle bootJar 

When we run that jar file in docker, docker accept only lowercase. So, we need to change jar file name.

The following codes are renaming the jar file in build.gradle.kts file.

  &nbsp;&nbsp;&nbsp;&nbsp;tasks.bootJar {
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;archiveBaseName.set("heartlink")
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;archiveVersion.set("1.0.2")
  
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;archiveClassifier.set("")
  
  &nbsp;&nbsp;&nbsp;&nbsp;}

(2)	Create Docker file in project directory

(3)	Create docker-compose.ymal

(4)	Build docker image for back-end API

    docker build -t hninhninwai/heartlink:1.0.1  .

![image](https://github.com/user-attachments/assets/d46cc4d5-5f7d-4895-98e8-d5e4997a7d38)

(5)	Push that back-end image to Docker Hub (https://hub.docker.com/)

    docker push hninhninwai/heartlink:1.0.1

![image](https://github.com/user-attachments/assets/9f88d24e-1c8d-44cb-816d-9465016a4adf)

![image](https://github.com/user-attachments/assets/b1bfbb91-d139-4c53-b89c-cd1dfa52bf59)

![image](https://github.com/user-attachments/assets/09f47947-1088-4bfe-80d8-e0e011a417c9)

(6)	Please Sign In to https://portal.azure.com/ , Go to Home-> App Services->Create->Web App

![image](https://github.com/user-attachments/assets/4c5a78c1-833e-4de7-9ae8-8d1b170e5599)

After choosing Web App, please select container.

![image](https://github.com/user-attachments/assets/f90ac4c4-7986-4843-9490-3c15ba208d1a)

After created web app,  clicked Deployment Center. And Please Paste data from docker-compose.yaml and Save.

![image](https://github.com/user-attachments/assets/28537c2d-0ca9-4f2c-8ace-45224ca5fccf)

When the deployment succeed, you can see the following logs.

![image](https://github.com/user-attachments/assets/b984c1ea-b4a8-4a91-98f9-ba0c6dea7785)


















