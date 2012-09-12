cd /root/twitstreet
git pull
service tomcat6 stop
rm -rf /var/lib/tomcat6/webapps/ROOT
ant -Ddest-file=/var/lib/tomcat6/webapps/ROOT.war
service tomcat6 start

