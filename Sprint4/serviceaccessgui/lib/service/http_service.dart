class HttpService {
  final String stockURL = "http://localhost:8080/getList";
/*
  Future<void> sampleFunction() async {
    Response res = await get(Uri.parse(stockURL));
    return;
  }

  Future<void> storeFood(double quantity) async {
    Response res = await get(Uri.parse(stockURL));
    return;
  }


  Future<List<Stock>> getStocks() async {
    Response res = await get(Uri.parse(stockURL));

    if (res.statusCode == 200) {
      final obj = jsonDecode(res.body);

      print(obj['stock'][0]['symbol']);

      List<Stock> stocks = new List<Stock>();

      for (int i = 0; i < obj['stock'].length; i++) {
        Stock stock = new Stock(
            company: obj['stock'][i]['name'],
            symbol: obj['stock'][i]['symbol'],
            price: obj['stock'][i]['price'],
            chg: obj['stock'][i]['chg']);

        stocks.add(stock);
      }

      return stocks;
    } else {
      throw "Unable to retrieve stock data.";
    }
  }

   */
}
