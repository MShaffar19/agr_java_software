all:
	mvn -T 4 clean package

api:
	mvn -T 4 clean package -pl agr_api -am

cacher:
	mvn -T 4 clean package -pl agr_cacher -am

dataextractor:
	mvn -T 4 clean package -pl agr_intermine_data_extractor -am

cacherq:
	mvn -T 4 -q clean package -pl agr_cacher -am

cacherrun:
	java -jar agr_cacher/target/agr_cacher-jar-with-dependencies.jar

apiq:
	mvn -T 4 -q clean package -pl agr_api -am

apirun:
	make -C agr_api run

apidebug:
	make -C agr_api debug

indexer:
	mvn -T 4 clean package -pl agr_indexer -am

variant_indexer:
	mvn -T 4 clean package -pl agr_variant_indexer -am

esutil:
	mvn -T 4 clean package -pl agr_elasticsearch_util -am

core:
	mvn -T 4 clean package -pl agr_java_core -am

searchtest:
	mvn -q -T 4 -Dit.test="AutocompteIntegrationSpec,QueryMatchIntegrationSpec,QueryRankIntegrationSpec,QueryTokenizationIntegrationSpec,RelatedDataServiceIntegrationSpec" -DfailIfNoTests=false verify
