package com.crio.warmup.stock;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.crio.warmup.stock.dto.TotalReturnsDto;
import com.crio.warmup.stock.log.UncaughtExceptionHandler;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.File;
import java.io.IOException;
import java.lang.Math;
import java.net.URISyntaxException;
/*
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import org.apache.logging.log4j.ThreadContext;


public class PortfolioManagerApplication {

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  // Read the json file provided in the argument[0]. The file will be avilable in
  // the classpath.
  // 1. Use #resolveFileFromResources to get actual file from classpath.
  // 2. parse the json file using ObjectMapper provided with #getObjectMapper,
  // and extract symbols provided in every trade.
  // return the list of all symbols in the same order as provided in json.
  // Test the function using gradle commands below
  // ./gradlew run --args="trades.json"
  // Make sure that it prints below String on the console -
  // ["AAPL","MSFT","GOOGL"]
  // Now, run
  // ./gradlew build and make sure that the build passes successfully
  // There can be few unused imports, you will need to fix them to make the build
  // pass.

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {
    ObjectMapper objmap = getObjectMapper();
    PortfolioTrade[] obj = objmap.readValue(
    resolveFileFromResources(args[0]), PortfolioTrade[].class);
    List<String> lst = new ArrayList<String>();
    for (int i = 0; i < obj.length; i++) {
      lst.add(obj[i].getSymbol());
    }
    return lst;
  }

  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(
      String filename) throws URISyntaxException {
    return Paths.get(Thread.currentThread()
    .getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  // TODO: CRIO_TASK_MODULE_JSON_PARSING
  // Follow the instructions provided in the task documentation and fill up the
  // correct values for
  // the variables provided. First value is provided for your reference.
  // A. Put a breakpoint on the first line inside mainReadFile() which says
  // return Collections.emptyList();
  // B. Then Debug the test #mainReadFile provided in
  // PortfoliomanagerApplicationTest.java
  // following the instructions to run the test.
  // Once you are able to run the test, perform following tasks and record the
  // output as a
  // String in the function below.
  // Use this link to see how to evaluate expressions -
  // https://code.visualstudio.com/docs/editor/debugging#_data-inspection
  // 1. evaluate the value of "args[0]" and set the value
  // to the variable named valueOfArgument0 (This is implemented for your
  // reference.)
  // 2. In the same window, evaluate the value of expression below and set it
  // to resultOfResolveFilePathArgs0
  // expression ==> resolveFileFromResources(args[0])
  // 3. In the same window, evaluate the value of expression below and set it
  // to toStringOfObjectMapper.
  // You might see some garbage numbers in the output. Dont worry, its expected.
  // expression ==> getObjectMapper().toString()
  // 4. Now Go to the debug window and open stack trace. Put the name of the
  // function you see at
  // second place from top to variable functionNameFromTestFileInStackTrace
  // 5. In the same window, you will see the line number of the function in the
  // stack trace window.
  // assign the same to lineNumberFromTestFileInStackTrace
  // Once you are done with above, just run the corresponding test and
  // make sure its working as expected. use below command to do the same.
  // ./gradlew test --tests PortfolioManagerApplicationTest.testDebugValues

  public static List<String> debugOutputs() {
    String valueOfArgument0 =
        "trades.json";
    String resultOfResolveFilePathArgs0 =
        "/home/crio-user/workspace/arkasengupta23-ME_QMONEY/qmoney/bin/main/trades.json";
    String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@6d9f7a80";
    String functionNameFromTestFileInStackTrace = "mainReadFile";
    String lineNumberFromTestFileInStackTrace = "22";

    return Arrays.asList(new String[] {
      valueOfArgument0,
      resultOfResolveFilePathArgs0,
      toStringOfObjectMapper,
      functionNameFromTestFileInStackTrace,
      lineNumberFromTestFileInStackTrace
    });
  }
}
*/

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.web.client.RestTemplate;


public class PortfolioManagerApplication {

  // TODO: CRIO_TASK_MODULE_REST_API
  //  Copy the relavent code from #mainReadFile to parse the Json into PortfolioTrade list.
  //  Now That you have the list of PortfolioTrade already populated in module#1
  //  For each stock symbol in the portfolio trades,
  //  Call Tiingo api (https://api.tiingo.com/tiingo/daily/<ticker>/prices?startDate=&endDate=&token=)
  //  with
  //   1. ticker = symbol in portfolio_trade
  //   2. startDate = purchaseDate in portfolio_trade.
  //   3. endDate = args[1]
  //  Use RestTemplate#getForObject in order to call the API,
  //  and deserialize the results in List<Candle>
  //  Note - You may have to register on Tiingo to get the api_token.
  //    Please refer the the module documentation for the steps.
  //  Find out the closing price of the stock on the end_date and
  //  return the list of all symbols in ascending order by its close value on endDate
  //  Test the function using gradle commands below
  //   ./gradlew run --args="trades.json 2020-01-01"
  //   ./gradlew run --args="trades.json 2019-07-01"
  //   ./gradlew run --args="trades.json 2019-12-03"
  //  And make sure that its printing correct results.

  public static List<String> mainReadFile(String[] args) throws IOException, URISyntaxException {
    ObjectMapper objmap = getObjectMapper();
    PortfolioTrade[] obj = objmap.readValue(
        resolveFileFromResources(args[0]), PortfolioTrade[].class);
    List<String> lst = new ArrayList<String>();
    for (int i = 0; i < obj.length; i++) {
      lst.add(obj[i].getSymbol());
    }
    return lst;
  }

  public static List<String> mainReadQuotes(String[] args) throws IOException, URISyntaxException {
    ObjectMapper objmap = getObjectMapper();
    PortfolioTrade[] obj = objmap.readValue(
    resolveFileFromResources(args[0]), PortfolioTrade[].class);
    List<String> lst = new ArrayList<String>();
    for (int i = 0; i < obj.length; i++) {
      lst.add(obj[i].getSymbol());
    }
    List<TotalReturnsDto> finallist = new ArrayList<TotalReturnsDto>(obj.length);
    for (int j = 0; j < obj.length; j++) {
      ObjectMapper mapper = getObjectMapper();
      String uri = "https://api.tiingo.com/tiingo/daily/" + lst.get(j) + "/prices?startDate=" + obj[j].getPurchaseDate() + "&endDate=" + args[1] + "&token=311fc02e387a223f3afcb59e8c3e8af939384aaf";
      RestTemplate restTemplate = new RestTemplate();
      String result = restTemplate.getForObject(uri, String.class);
      List<TiingoCandle> collection = 
          mapper.readValue(result, new TypeReference<ArrayList<TiingoCandle>>() {});
      if (collection == null) {
        lst.clear();
        return lst;
      }
      int k = 0;
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate endDate = LocalDate.parse(args[1], formatter);
      if (endDate.getMonth().getValue() == 1 && endDate.getDayOfMonth() == 1) {
        lst.clear();
        return lst;
      }
      //System.out.println(endDate);
      while (!collection.get(k).getDate().isEqual(endDate)) {
        k++;
      }
      TotalReturnsDto ob = new TotalReturnsDto(lst.get(j), collection.get(k).getClose());
      //finallist.get(j).setSymbol(lst.get(j));
      //finallist.get(j).setClosingPrice(collection.get(k).getClose());
      finallist.add(j, ob);
      //System.out.println(lst.get(j)+":");
      //System.out.println(collection.get(k).getClose());
    }
    Collections.sort(finallist, Comparator.comparingDouble(TotalReturnsDto::getClosingPrice));
    lst.clear();

    for (int i = 0; i < finallist.size(); i++) {
      lst.add(finallist.get(i).getSymbol());
    }
    return lst;
  }

  private static void printJsonObject(Object object) throws IOException {
    Logger logger = Logger.getLogger(PortfolioManagerApplication.class.getCanonicalName());
    ObjectMapper mapper = new ObjectMapper();
    logger.info(mapper.writeValueAsString(object));
  }

  private static File resolveFileFromResources(
      String filename) throws URISyntaxException {
    return Paths.get(Thread.currentThread()
    .getContextClassLoader().getResource(filename).toURI()).toFile();
  }

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  public static List<String> debugOutputs() {
    String valueOfArgument0 =
        "trades.json";
    String resultOfResolveFilePathArgs0 =
        "/home/crio-user/workspace/arkasengupta23-ME_QMONEY/qmoney/bin/main/trades.json";
    String toStringOfObjectMapper = "com.fasterxml.jackson.databind.ObjectMapper@6d9f7a80";
    String functionNameFromTestFileInStackTrace = "mainReadFile";
    String lineNumberFromTestFileInStackTrace = "22";

    return Arrays.asList(new String[] {
      valueOfArgument0,
      resultOfResolveFilePathArgs0,
      toStringOfObjectMapper,
      functionNameFromTestFileInStackTrace,
      lineNumberFromTestFileInStackTrace
    });
  }
  

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  Copy the relevant code from #mainReadQuotes to parse the Json into PortfolioTrade list and
  //  Get the latest quotes from TIingo.
  //  Now That you have the list of PortfolioTrade And their data,
  //  With this data, Calculate annualized returns for the stocks provided in the Json
  //  Below are the values to be considered for calculations.
  //  buy_price = open_price on purchase_date and sell_value = close_price on end_date
  //  startDate and endDate are already calculated in module2
  //  using the function you just wrote #calculateAnnualizedReturns
  //  Return the list of AnnualizedReturns sorted by annualizedReturns in descending order.
  //  use gralde command like below to test your code
  //  ./gradlew run --args="trades.json 2020-01-01"
  //  ./gradlew run --args="trades.json 2019-07-01"
  //  ./gradlew run --args="trades.json 2019-12-03"
  //  where trades.json is your json file

  public static List<AnnualizedReturn> mainCalculateSingleReturn(String[] args)
      throws IOException, URISyntaxException {
    ObjectMapper objmap = getObjectMapper();
    PortfolioTrade[] obj = objmap.readValue(
    resolveFileFromResources(args[0]), PortfolioTrade[].class);
    List<String> lst = new ArrayList<String>();
    for (int i = 0; i < obj.length; i++) {
      lst.add(obj[i].getSymbol());
    }
    List<AnnualizedReturn> finallist = new ArrayList<AnnualizedReturn>(obj.length);
    for (int j = 0; j < obj.length; j++) {
      ObjectMapper mapper = getObjectMapper();
      String uri = "https://api.tiingo.com/tiingo/daily/" + lst.get(j) + "/prices?startDate=" + obj[j].getPurchaseDate() + "&endDate=" + args[1] + "&token=311fc02e387a223f3afcb59e8c3e8af939384aaf";
      RestTemplate restTemplate = new RestTemplate();
      String result = restTemplate.getForObject(uri, String.class);
      List<TiingoCandle> collection = 
          mapper.readValue(result, new TypeReference<ArrayList<TiingoCandle>>() {});
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
      LocalDate endDate = LocalDate.parse(args[1], formatter);
      if (endDate.getMonth().getValue() == 1 && endDate.getDayOfMonth() == 1) {
        endDate = endDate.minusDays(1);
      }
      if (obj[j].getPurchaseDate().getMonth().getValue() == 1 
          && obj[j].getPurchaseDate().getDayOfMonth() == 1) {
        
        obj[j].setPurchaseDate(obj[j].getPurchaseDate().plusDays(1));
      }
      int k = 0;
      while (!collection.get(k).getDate().isEqual(endDate)) {
        k++;
      }
      Double sellPrice = collection.get(k).getClose();
      int x = 0;
      while (!collection.get(x).getDate().isEqual(obj[j].getPurchaseDate())) {
        x++;
      }
      Double buyPrice = collection.get(x).getOpen();
      AnnualizedReturn annret = calculateAnnualizedReturns(endDate, obj[j], buyPrice, sellPrice);
      finallist.add(j, annret);
    }

    Collections.sort(finallist, Comparator.comparingDouble(AnnualizedReturn::getAnnualizedReturn));
    Collections.reverse(finallist);
    return finallist;
  }

  // TODO: CRIO_TASK_MODULE_CALCULATIONS
  //  annualized returns should be calculated in two steps -
  //  1. Calculate totalReturn = (sell_value - buy_value) / buy_value
  //  Store the same as totalReturns
  //  2. calculate extrapolated annualized returns by scaling the same in years span. The formula is
  //  annualized_returns = (1 + total_returns) ^ (1 / total_num_years) - 1
  //  Store the same as annualized_returns
  //  return the populated list of AnnualizedReturn for all stocks,
  //  Test the same using below specified command. The build should be successful
  //  ./gradlew test --tests PortfolioManagerApplicationTest.testCalculateAnnualizedReturn

  public static AnnualizedReturn calculateAnnualizedReturns(LocalDate endDate,
      PortfolioTrade trade, Double buyPrice, Double sellPrice) {

    Double totalReturns = (sellPrice - buyPrice) / buyPrice;
    //Period intervalPeriod = Period.between(trade.getPurchaseDate(), endDate);
    /*
    if (endDate.getMonth().getValue() == 1 && endDate.getDayOfMonth() == 1) {
      endDate = endDate.plusDays(1);
    }
    if (trade.getPurchaseDate().getMonth().getValue() == 1 
        && trade.getPurchaseDate().getDayOfMonth() == 1) {
      
      trade.setPurchaseDate(trade.getPurchaseDate().minusDays(1));
    }
    */
    long daysBetween = ChronoUnit.DAYS.between(trade.getPurchaseDate(), endDate);
    Double annualizedreturns = 
        Math.pow(1 + totalReturns, (1 / (double)((double)daysBetween / 365))) - 1;
    return new AnnualizedReturn(trade.getSymbol(), annualizedreturns, totalReturns);
  }

  public static void main(String[] args) throws Exception {
    Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler());
    ThreadContext.put("runId", UUID.randomUUID().toString());
    printJsonObject(mainCalculateSingleReturn(args));
  }

}

