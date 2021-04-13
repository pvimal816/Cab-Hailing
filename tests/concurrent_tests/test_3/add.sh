for i in {0..10};
do
	resp=$(curl -s "http://10.106.181.133:8082/addAmount?custId=201&amount=100")
done