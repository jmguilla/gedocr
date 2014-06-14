// locations to search for config files that get merged into the main config;
// config files can be ConfigSlurper scripts, Java properties files, or classes
// in the classpath in ConfigSlurper format

// grails.config.locations = [ "classpath:${appName}-config.properties",
//                             "classpath:${appName}-config.groovy",
//                             "file:${userHome}/.grails/${appName}-config.properties",
//                             "file:${userHome}/.grails/${appName}-config.groovy"]

// if (System.properties["${appName}.config.location"]) {
//    grails.config.locations << "file:" + System.properties["${appName}.config.location"]
// }
grails.app.name = "fr"
grails.app.context = "/"
grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination

// The ACCEPT header will not be used for content negotiation for user agents containing the following strings (defaults to the 4 major rendering engines)
grails.mime.disable.accept.header.userAgents = [
  'Gecko',
  'WebKit',
  'Presto',
  'Trident'
]
grails.mime.types = [
  all:           '*/*',
  atom:          'application/atom+xml',
  css:           'text/css',
  csv:           'text/csv',
  form:          'application/x-www-form-urlencoded',
  html:          [
    'text/html',
    'application/xhtml+xml'
  ],
  js:            'text/javascript',
  json:          [
    'application/json',
    'text/json'
  ],
  multipartForm: 'multipart/form-data',
  rss:           'application/rss+xml',
  text:          'text/plain',
  hal:           [
    'application/hal+json',
    'application/hal+xml'
  ],
  xml:           [
    'text/xml',
    'application/xml']
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = [
  '/images/*',
  '/css/*',
  '/js/*',
  '/plugins/*'
]

// Legacy setting for codec used to encode data with ${}
grails.views.default.codec = "html"

// The default scope for controllers. May be prototype, session or singleton.
// If unspecified, controllers are prototype scoped.
grails.controllers.defaultScope = 'singleton'

// GSP settings
grails {
  views {
    gsp {
      encoding = 'UTF-8'
      htmlcodec = 'xml' // use xml escaping instead of HTML4 escaping
      codecs {
        expression = 'html' // escapes values inside ${}
        scriptlet = 'html' // escapes output from scriptlets in GSPs
        taglib = 'none' // escapes output from taglibs
        staticparts = 'none' // escapes output from static template parts
      }
    }
    // escapes all not-encoded output at final stage of outputting
    filteringCodecForContentType {
      //'text/html' = 'html'
    }
  }
}

grails.converters.encoding = "UTF-8"
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// configure auto-caching of queries by default (if false you can cache individual queries with 'cache: true')
grails.hibernate.cache.queries = false

environments {
  development {
    grails.logging.jul.usebridge = true
    if(!System.getenv("APP_LAN_URL")){
      println "/!\\ Note that OAuth features require an appropriate server url /!\\"
      println "Current config: http://localhost:9090"
      println "No LAN address set, you can do it by setting APP_LAN_URL environment variable"
    }else{
      grails.serverURL = System.getenv("APP_LAN_URL")
    }
  }
  // TODO change that id
  production {
    grails.logging.jul.usebridge = false
    grails.serverURL = "http://app-shortener.rhcloud.com"
  }
}

// log4j configuration
log4j = {
  // Example of changing the log pattern for the default console appender:
  //
  //appenders {
  //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
  //}

  error  'org.codehaus.groovy.grails.web.servlet',        // controllers
      'org.codehaus.groovy.grails.web.pages',          // GSP
      'org.codehaus.groovy.grails.web.sitemesh',       // layouts
      'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
      'org.codehaus.groovy.grails.web.mapping',        // URL mapping
      'org.codehaus.groovy.grails.commons',            // core / classloading
      'org.codehaus.groovy.grails.plugins',            // plugins
      'org.codehaus.groovy.grails.orm.hibernate',      // hibernate integration
      'org.springframework',
      'org.hibernate',
      'net.sf.ehcache.hibernate'
}


// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.kott.fr.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.kott.fr.UserRole'
grails.plugin.springsecurity.authority.className = 'com.kott.fr.Role'
grails.plugin.springsecurity.userLookup.usernamePropertyName = 'email'


grails.plugin.springsecurity.facebook.domain.classname='com.kott.fr.FBUser'
grails.plugin.springsecurity.facebook.appId='697131890310012'
if(!System.getenv("FB_APP_SECRET")){
  throw new IllegalStateException("Please set FB_APP_SECRET environment variable.")
}
grails.plugin.springsecurity.facebook.secret=System.getenv("FB_APP_SECRET")
grails.plugin.springsecurity.securityConfigType = "Annotation"
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
  '/':                              ['permitAll'],
  '/oauth/*':                       ['permitAll'],
  '/partials/**':					['permitAll'],
  '/login/**':                      ['permitAll'],
  '/logout/**':                     ['permitAll'],
  '/dbconsole/*':                   ['permitAll'],
  '/emailConfirmation/*':           ['permitAll'],
  '/**/js/**':                      ['permitAll'],
  '/**/css/**':                     ['permitAll'],
  '/**/images/**':                  ['permitAll'],
  '/**/favicon.ico':                ['permitAll']
  ]

grails.plugin.springsecurity.roleHierarchy = '''
   ROLE_ADMIN > ROLE_USER
'''

environments {
  production {
    grails.plugin.databasemigration.updateOnStart = true
    grails.plugin.databasemigration.updateOnStartFileNames = ["changelog.groovy"]
  }
}

if(!System.getenv("APP_MAIL_USER") || !System.getenv("APP_MAIL_PWD")){
  println "/!\\ set APP_MAIL_USER & APP_MAIL_PWD if you want email to work /!\\"
}

grails{
  mail{
    username = System.getenv("APP_MAIL_USER")
    password = System.getenv("APP_MAIL_PWD")
    host = "smtp.gmail.com"
    port = 465
    props = ["mail.smtp.auth":"true",
      "mail.smtp.socketFactory.port":"465",
      "mail.smtp.socketFactory.class":"javax.net.ssl.SSLSocketFactory",
      "mail.smtp.socketFactory.fallback":"false"]
  }
}


// Config for google analytics integration
if(!System.getenv("google-privatekey.base64")){
  throw new IllegalStateException('google-privatekey.base64 env property is missing')
}
//TODO defined but not used... Problem in AnalyticsService at initializing time
google.analytics.accountId = "1051867773103-ujcp2cs6gok2jopa9iop3uetav918a68@developer.gserviceaccount.com"
google.analytics.statsProfileId = "ga:83491938"

grails.plugin.springsecurity.facebook.filter.processUrl = '/fb/j_spring_security_facebook_check'
grails.plugin.springsecurity.facebook.filter.redirect.redirectFromUrl = '/fb/j_spring_security_facebook_redirect'

def baseURL = grails.serverURL ?: "http://localhost:${System.getProperty('server.port', '8080')}/${appName}"

oauth {
	debug = true
	providers {
		google {
			api = org.grails.plugin.springsecurity.oauth.GoogleApi20
			key = '1051867773103-nvcs8uh41q7vdsaod5anti265dl5b9rq.apps.googleusercontent.com'
			secret = 'amNOkyEhXqVqahPs0hNICSE1'
            successUri = '/oauth/success?provider=google'
            failureUri = '/oauth/failure'
			callback = "${baseURL}/oauth/google/callback"
			scope = 'https://www.googleapis.com/auth/userinfo.profile https://www.googleapis.com/auth/userinfo.email'
		}
	}
}

grails.plugin.springsecurity.oauth.active = true
grails.plugin.springsecurity.oauth.domainClass = 'com.kott.fr.OAuthID'
grails.plugin.springsecurity.oauth.userLookup.oAuthIdsPropertyName = 'oAuthIDs'
grails.plugin.springsecurity.oauth.registration.askToLinkOrCreateAccountUri = '/oauth/askToLinkOrCreateAccount'
grails.plugin.springsecurity.oauth.registration.roleNames = ['ROLE_USER']
// Added by the Spring Security OAuth plugin:
