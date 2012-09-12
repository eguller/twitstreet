cd /root/twitstreet
/usr/bin/git pull
/etc/init.d/tomcat6 stop
rm -rf /var/lib/tomcat6/webapps/ROOT
ant -Ddest-file=/var/lib/tomcat6/webapps/ROOT.war -Dtomcat.home=/usr/share/tomcat6
/etc/init.d/tomcat6 start

