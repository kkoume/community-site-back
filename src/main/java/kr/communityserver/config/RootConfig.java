package kr.communityserver.config;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Getter
@Setter
@Configuration
@EnableAspectJAutoProxy
public class RootConfig {

    @Autowired
    private final BuildProperties buildProperties;

    public RootConfig(BuildProperties buildProperties) {
        this.buildProperties = buildProperties;
    }

    @Bean
    public ModelMapper modelMapper(){

        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) // Entity의 @Setter 선언없이 바로 private 속성으로 초기화 설정
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true);

        return modelMapper;
    }

    @Bean
    public AppInfo appInfo() {
        String name = buildProperties.getName();
        String version = buildProperties.getVersion();

        return new AppInfo(name, version);
    }

}