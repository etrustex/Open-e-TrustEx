# New eTrustEx Platform using Spring 4



## Prerequisites
- Jdk 8
- Maven 3+

## Development guide

### 1. Clone this repo from your IDE or command line (git clone)

You can obtain the url from this page's menu 'Actions' --> 'Clone' 

It should look like: https://YOUR_USERNAME@webgate.ec.europa.eu/CITnet/stash/scm/etrustex/etx-node.git
    
### 2. Build project

#### 2.1 See [etrustex-admin README](etrustex-admin/README.md) first to build the admin console.  

#### 2.2 Configure maven settings (`.m2/settings.xml`)
```xml
<?xml version="1.0" encoding="UTF-8"?>
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0" 
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd">

  <localRepository>C:\Your location for\.m2\repository</localRepository>

      <pluginGroups>
         <pluginGroup>com.oracle.weblogic</pluginGroup>
      </pluginGroups>

        <proxies>
            <proxy>
              <id>optional</id>
              <active>true</active>
              <protocol>http</protocol>
              <username>USERNAME</username>
              <password>PASSWORD</password>
             <host>147.67.138.13</host>
              <port>8012</port>
              <nonProxyHosts>localhost|127.0.0.1|citnet.cec.eu.int</nonProxyHosts>
            </proxy>
        </proxies>
        <servers>
            <server>
              <id>nexus-ec</id>
              <username>USERNAME</username>
              <password>PASSWORD</password>
            </server>
            <server>
              <id>nexus-digit</id>
              <username>USERNAME</username>
              <password>PASSWORD</password>
            </server>    
            <server>
              <id>nexus-digit-snapshots</id>
              <username>USERNAME</username>
              <password>PASSWORD</password>
            </server>    
            <server>
              <id>refapp</id>
              <username>USERNAME</username>
              <password>PASSWORD</password>
            </server> 
            <server>
              <id>refapp-snapshots</id>
              <username>USERNAME</username>
              <password>PASSWORD</password>
            </server> 
        </servers>
    <mirrors>
  </mirrors>
  <profiles>
    <profile>
        <id>location-commission</id>
        <activation>
            <activeByDefault>true</activeByDefault>
            <property>
                <name>env.USERDOMAIN</name>
                <value>NET1</value>
            </property>
       </activation>
       <repositories>
            <repository>
                <id>nexus-ec</id>
                <url>https://webgate.ec.europa.eu/CITnet/nexus/content/groups/public</url>
                <releases>
                    <enabled>true</enabled>
                </releases>
                <snapshots>
                    <enabled>false</enabled>
                </snapshots>
            </repository>
            <repository>
                <id>nexus-digit</id>
                <url>https://webgate.ec.europa.eu/CITnet/nexus/content/repositories/digit</url>
                <releases>
                    <enabled>false</enabled>
                </releases>
                <snapshots>
                    <enabled>true</enabled>
                </snapshots>
            </repository>
            <repository>
                <id>nexus-digit-snapshots</id>
                <url>https://webgate.ec.europa.eu/CITnet/nexus/content/repositories/digit-snapshots</url>
                <releases>
                    <enabled>false</enabled>
                </releases>
                <snapshots>
                    <enabled>true</enabled>
                </snapshots>
            </repository>
            <repository>
              <id>refapp</id>
              <name>Refapp Repository</name>
              <url>https://webgate.ec.europa.eu/CITnet/nexus/content/repositories/digit</url>
            </repository>
            <repository>
              <id>refapp-snapshots</id>
              <name>Refapp Snapshots Repository</name>
              <url>https://webgate.ec.europa.eu/CITnet/nexus/content/repositories/refapp-snapshots</url>
                        <snapshots>
                            <enabled>true</enabled>
                        </snapshots>          
            </repository>                 
        </repositories>
        <pluginRepositories>
            <pluginRepository>
                <id>nexus-ec</id>
                <url>https://webgate.ec.europa.eu/CITnet/nexus/content/groups/public/</url>
                <releases>
                    <enabled>true</enabled>
                </releases>
                <snapshots>
                    <enabled>true</enabled>
                </snapshots>
            </pluginRepository>
        </pluginRepositories>
    </profile>
  </profiles>
</settings>
```


#### 2.3 Run maven build
   In the project root (parent pom.xml) run:
   ```sh
    mvn clean install -P LOCAL-WEBLO,weblogic,oracle,openetrustex
   ```
   
### 3. Deploy on Weblogic 
   If you have WL_DOMAIN_HOME environment variable already configured, you don't need to do anything.  
   Otherwise, you need to copy etrustex-ear/target/etrustex.ear in the /autodeploy folder of your WL domain.