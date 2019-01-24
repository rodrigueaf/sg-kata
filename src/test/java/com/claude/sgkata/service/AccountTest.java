package com.claude.sgkata.service;

import com.claude.sgkata.ApplicationTest;
import com.claude.sgkata.controller.AccountController;
import com.claude.sgkata.controller.CustomControllerAdvice;
import com.claude.sgkata.util.DataSourceUtil;
import com.claude.sgkata.util.TestUtil;
import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class AccountTest extends ApplicationTest {

    private MockMvc mockMvc;
    @Autowired
    private CustomControllerAdvice customControllerAdvice;
    @Autowired
    private MappingJackson2HttpMessageConverter jackson2HttpMessageConverter;
    @Autowired
    private AccountService accountService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AccountController accountController = new AccountController(accountService);
        mockMvc = MockMvcBuilders.standaloneSetup(accountController)
                .setMessageConverters(jackson2HttpMessageConverter)
                .setControllerAdvice(customControllerAdvice)
                .build();
        DataSourceUtil.initTransactions();
    }

    @Test
    public void shouldBalanceEncreaseBy50WhenDepositIs50() throws Exception {
        double depositeAmount = 50.0;
        double expectedBalance = 150.0;
        String accountNumber = DataSourceUtil.accounts.get(0);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{number}/transactions", accountNumber)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(depositeAmount)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").value(expectedBalance));
        Assertions.assertThat(accountService.getBalance(accountNumber)).isEqualTo(expectedBalance);
    }

    @Test
    public void shouldBalanceDecreaseBy50WhenWithdrawalIs50() throws Exception {
        double withdrawalAmount = -50.0;
        double expectedBalance = 50.0;
        String accountNumber = DataSourceUtil.accounts.get(0);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{number}/transactions", accountNumber)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(withdrawalAmount)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").value(expectedBalance));
        Assertions.assertThat(accountService.getBalance(accountNumber)).isEqualTo(expectedBalance);
    }

    @Test
    public void shouldNotDecreaseBalanceWhenBalanceIsInsufficient() throws Exception {
        double withdrawalAmount = -150.0;
        double expectedBalance = 100.0;
        String accountNumber = DataSourceUtil.accounts.get(0);
        mockMvc.perform(MockMvcRequestBuilders.post("/accounts/{number}/transactions", accountNumber)
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(withdrawalAmount)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$").value("Insufficient balance to make this withdraw"));
        Assertions.assertThat(accountService.getBalance(accountNumber)).isEqualTo(expectedBalance);
    }

    @Test
    public void shouldReturnTransactionsList() throws Exception {
        String accountNumber = DataSourceUtil.accounts.get(0);
        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/{number}/transactions", accountNumber)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].accountNumber").value(CoreMatchers.hasItem(accountNumber)));
    }
}
