package com.cristborges.experimento_camel_tcc.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.UrlBasedViewResolver;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.springframework.web.servlet.view.tiles2.TilesView;

@Configuration
@ComponentScan("com.cristborges.experimento_camel_tcc")
@EnableWebMvc
public class ConfigWebMvc extends WebMvcConfigurerAdapter {

	@Bean
	public ViewResolver viewResolver() {
		UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
		urlBasedViewResolver.setViewClass(TilesView.class);
		return urlBasedViewResolver;
	}

	@Bean
	public TilesConfigurer tilesConfigurer() {
		TilesConfigurer tilesConfigurer = new TilesConfigurer();
		tilesConfigurer.setDefinitions(new String[] { "/WEB-INF/jsp/tiles.xml" });
		tilesConfigurer.setCheckRefresh(true);
		return tilesConfigurer;
	}

	@Bean
	public MessageSource messageSource() {
		CustomResourceBundleMessageSource customResourceBundleMessageSource = new CustomResourceBundleMessageSource();
		customResourceBundleMessageSource.setBasename("messages");
		return customResourceBundleMessageSource;
	}

	@Override
	public Validator getValidator() {
		LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();
		validator.setValidationMessageSource(messageSource());
		return validator;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry resourceHandlerRegistry) {
		resourceHandlerRegistry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

	private class CustomResourceBundleMessageSource extends ResourceBundleMessageSource {
		@Override
		protected String getMessageInternal(String code, Object[] args, Locale locale) {
			String result = super.getMessageInternal(code, args, locale);

			return (result == null || code == null || (!code.startsWith("NotEmpty") && !code.startsWith("Pattern") && !code.startsWith("NotNull"))) ? 
					null : 
					(result + "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-label=\"Close\"><span aria-hidden=\"true\">&times;</span></button>");
		}
	}
}
