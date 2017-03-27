package vr.com.apps.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import vr.com.apps.transactionLinking.model.entity.BankStatement;
import vr.com.apps.transactionLinking.model.entity.Customer;
import vr.com.apps.transactionLinking.model.report.ReportTransactionsHistory;
import vr.com.apps.transactionLinking.service.ResourceList;
import vr.com.apps.transactionLinking.service.ResourceListOrders;
import vr.com.apps.transactionLinking.service.banksTransactions.EBanksKinds;
import vr.com.apps.transactionLinking.service.banksTransactions.PBBankStatement;
import vr.com.apps.transactionLinking.service.banksTransactions.PBResourceListBankStatement;
import vr.com.apps.utility.resourceReader.ResourceProperty;
import vr.com.apps.utility.resourceReader.EResourceType;

import java.util.HashMap;
import java.util.Map;


@Configuration
@EnableWebMvc
@ComponentScan("vr.com.apps")
/*@PropertySource(value = { "classpath:application.properties" })*/
public class WebAppConfig extends WebMvcConfigurerAdapter {

    /*
    * PropertySourcesPlaceHolderConfigurer Bean only required for @Value("{}") annotations.
    * Remove this bean if you are not using @Value annotations for injecting properties.
    */

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    // Позволяет видеть все ресурсы в папке resources, такие как картинки, стили и т.п.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/WEB-INF/resources/");
    }

    // а этот бин инициализирует View нашего проекта
    @Bean
    public InternalResourceViewResolver setupViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/pages/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);

        return resolver;
    }

    @Bean
    public Customer emptyCustomer(){
        Customer customer = new Customer();
        customer.setId("P00000");
        return customer;
    }

    @Bean
    public ReportTransactionsHistory reportTransactionsHistory() {
        return new ReportTransactionsHistory("Transactions history");
    }

    @Bean
    public ResourceListOrders orderList(){
        return new ResourceListOrders<>(new ResourceProperty(EResourceType.FILE));
    }

    @Bean
    public Map<EBanksKinds, ResourceList<BankStatement>> bankStatementsList(){
        Map<EBanksKinds, ResourceList<BankStatement>> bankStatementsList = new HashMap<>();
        bankStatementsList.put(EBanksKinds.PB, new PBResourceListBankStatement<>(new ResourceProperty(EResourceType.FILE)));
        return bankStatementsList;
    }

    @Bean
    public ResourceList<PBBankStatement> resourceListBankStatementPB(){
        return  new PBResourceListBankStatement<>(new ResourceProperty(EResourceType.FILE));
    }
}
