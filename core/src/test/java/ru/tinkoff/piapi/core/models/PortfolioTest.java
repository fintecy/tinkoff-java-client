package ru.tinkoff.piapi.core.models;

import org.junit.jupiter.api.Test;
import ru.tinkoff.piapi.contract.v1.MoneyValue;

import static org.junit.jupiter.api.Assertions.*;
import static ru.tinkoff.piapi.contract.v1.PortfolioResponse.newBuilder;
import static ru.tinkoff.piapi.core.models.Portfolio.fromResponse;

class PortfolioTest {
  @Test
  void should_sum_up_total_position() {
    //given
    var portfolio = fromResponse(newBuilder()
      .setTotalAmountBonds(getValue(2L))
      .setTotalAmountShares(getValue(4L))
      .setTotalAmountFutures(getValue(8L))
      .setTotalAmountEtf(getValue(16L))
      .setTotalAmountCurrencies(getValue(32L)).build());
    var expected = Money.fromResponse(getValue(62L));

    //when
    var actual = portfolio.getTotalPortfolioValue();

    assertEquals(actual, expected);
  }

  @Test
  void should_fail_to_sum_different_currencies() {
    //given
    var portfolio = fromResponse(newBuilder()
      .setTotalAmountBonds(getValue(2L, "USD"))
      .setTotalAmountShares(getValue(4L))
      .setTotalAmountFutures(getValue(8L))
      .setTotalAmountEtf(getValue(16L))
      .setTotalAmountCurrencies(getValue(32L)).build());
    var expected = Money.fromResponse(getValue(62L));

    //when
    assertThrows(IllegalArgumentException.class, portfolio::getTotalPortfolioValue);
  }

  private MoneyValue getValue(long value) {
    return getValue(value, "RUB");
  }

  private MoneyValue getValue(long value, String ccy) {
    return MoneyValue.newBuilder().setCurrency(ccy).setUnits(value).build();
  }
}
