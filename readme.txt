TmapiStore is a read-only Sesame Sail [1] implementation
to enable a RDF view on the ISO/IEC 13250 Topic Maps [2] standard.
TmapiStore supports every TMAPI 2.0 [3] compatible engine as back-end
and follows the the mapping approach developed by NetworkedPlanet [4]. 

Running the following m2 command generates configuration files for your IDE:
Eclipse: 		mvn eclipse:eclipse
Netbeans: 		mvn netbeans-freeform:generate-netbeans-project
IntelliJ IDEA: 	mvn idea:idea


To build the project, run the unit tests and install it locally to your repository execute:
mvn clean install

1) http://www.openrdf.org/
2) http://www.isotopicmaps.org/
3) http://www.tmapi.org/2.0/
4) http://www.networkedplanet.com/ontopic/2009/11/making_topic_maps_sparql.html