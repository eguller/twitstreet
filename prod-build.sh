cd /root/twitstreet
/usr/bin/git pull
/etc/init.d/tomcat6 stop
rm -rf /var/lib/tomcat6/webapps/ROOT
ant -Ddest-file=/var/lib/tomcat6/webapps/ROOT.war
/etc/init.d/tomcat6 start

