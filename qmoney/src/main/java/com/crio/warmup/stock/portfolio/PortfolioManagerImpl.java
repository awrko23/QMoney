package com.crio.warmup.stock.portfolio;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.SECONDS;

import com.crio.warmup.stock.dto.AnnualizedReturn;
import com.crio.warmup.stock.dto.Candle;
import com.crio.warmup.stock.dto.PortfolioTrade;
import com.crio.warmup.stock.dto.TiingoCandle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.springframework.web.client.RestTemplate;

public class PortfolioManagerImpl implements PortfolioManager {




  private Object restTemplate;

  // Caution: Do not delete or modify the constructor, or else your build will
  // break!
  // This is absolutely necessary for backward compatibility
  protected PortfolioManagerImpl(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
  }


  //TODO: CRIO_TASK_MODULE_REFACTOR
  // Now we want to convert our code into a module, so we will not call it from main anymore.
  // Copy your code from Module#3 PortfolioManagerApplication#calculateAnnualizedReturn
  // into #calculateAnnualizedReturn function here and make sure that it
  // follows the method signature.
  // Logic to read Json file and convert them into Objects will not be required further as our
  // clients will take care of it, going forward.
  // Test your code using Junits provided.
  // Make sure that all of the tests inside PortfolioManagerTest using command below -
  // ./gradlew test --tests PortfolioManagerTest
  // This will guard you against any regressions.
  // run ./gradlew build in order to test yout code, and make sure that
  // the tests and static code quality pass.

  //CHECKSTYLE:OFF




  private Comparator<AnnualizedReturn> getComparator() {
    return Comparator.comparing(AnnualizedReturn::getAnnualizedReturn).reversed();
  }

  //CHECKSTYLE:OFF

  // TODO: CRIO_TASK_MODULE_REFACTOR
  //  Extract the logic to call Tiingo thirdparty APIs to a separate function.
  //  It should be split into fto parts.
  //  Part#1 - Prepare the Url to call Tiingo based on a template constant,
  //  by replacing the placeholders.
  //  Constant should look like
  //  https://api.tiingo.com/tiingo/daily/<ticker>/prices?startDate=?&endDate=?&token=?
  //  Where ? are replaced with something similar to <ticker> and then actual url produced by
  //  replacing the placeholders with actual parameters.

  private static ObjectMapper getObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }
  
  public List<Candle> getStockQuote(String symbol, LocalDate from, LocalDate to)
      throws JsonProcessingException {
        ObjectMapper mapper = getObjectMapper();
        String uri = buildUri(symbol, from, to);
        String result = ((RestTemplate) this.restTemplate).getForObject(uri, String.class);
        List<TiingoCandle> collection = 
          mapper.readValue(result, new TypeReference<ArrayList<TiingoCandle>>() {});
        List<Candle> finallist = new ArrayList<Candle>(collection);
        return finallist;
  }

  protected String buildUri(String symbol, LocalDate startDate, LocalDate endDate) {
       String uriTemplate = "https://api.tiingo.com/tiingo/daily/" + symbol + "/prices?"
            + "startDate=" + startDate.toString() + "&endDate=" + endDate.toString() + "&token=311fc02e387a223f3afcb59e8c3e8af939384aaf";
       return uriTemplate;
  }

  public List<AnnualizedReturn> calculateAnnualizedReturn(List<PortfolioTrade> portfolioTrades,
      LocalDate endDate) throws JsonProcessingException {
        Double sellPrice, buyPrice;
        List<AnnualizedReturn> annulist = new ArrayList<AnnualizedReturn>(portfolioTrades.size());
        for(int i = 0; i < portfolioTrades.size(); i++) {
          List<Candle> stocklist = getStockQuote(portfolioTrades.get(i).getSymbol(), portfolioTrades.get(i).getPurchaseDate(), endDate);
          if (endDate.getMonth().getValue() == 1 && endDate.getDayOfMonth() == 1) {
            endDate = endDate.minusDays(1);
          }
          if (portfolioTrades.get(i).getPurchaseDate().getMonth().getValue() == 1 
              && portfolioTrades.get(i).getPurchaseDate().getDayOfMonth() == 1) {
            
            portfolioTrades.get(i).setPurchaseDate(portfolioTrades.get(i).getPurchaseDate().plusDays(1));
          }
          int k = 0;
          while (!stocklist.get(k).getDate().isEqual(endDate)) {
            k++;
          }
          sellPrice = stocklist.get(k).getClose();
          int x = 0;
          while (!stocklist.get(x).getDate().isEqual(portfolioTrades.get(i).getPurchaseDate())) {
            x++;
          }
          buyPrice = stocklist.get(x).getOpen();
          Double totalReturns = (sellPrice - buyPrice) / buyPrice;
          long daysBetween = ChronoUnit.DAYS.between(portfolioTrades.get(i).getPurchaseDate(), endDate);
          Double annualizedreturns = 
              Math.pow(1 + totalReturns, (1 / (double)((double)daysBetween / 365))) - 1;
          annulist.add(new AnnualizedReturn(portfolioTrades.get(i).getSymbol(), annualizedreturns, totalReturns));
        }
        Collections.sort(annulist, getComparator());
        return annulist;
  }
}
