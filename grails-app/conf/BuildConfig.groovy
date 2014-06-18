grails.servlet.version = "3.0" // Change depending on target container compliance (2.5 or 3.0)
grails.project.work.dir = "target"
grails.project.target.level = 1.6
grails.project.source.level = 1.6
grails.war.exploded = true
grails.project.war.exploded.dir = "../shortener-cartridge/webapps/ROOT"
//grails.project.war.file = "target/${appName}-${appVersion}.war"

grails.project.fork = [
  // configure settings for compilation JVM, note that if you alter the Groovy version forked compilation is required
  //  compile: [maxMemory: 256, minMemory: 64, debug: false, maxPerm: 256, daemon:true],

  // configure settings for the test-app JVM, uses the daemon by default
  test: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, daemon:true],
  // configure settings for the run-app JVM
  run: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
  // configure settings for the run-war JVM
  war: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256, forkReserve:false],
  // configure settings for the Console UI JVM
  console: [maxMemory: 768, minMemory: 64, debug: false, maxPerm: 256]
]

grails.project.dependency.resolver = "maven" // or ivy
grails.project.dependency.resolution = {
  // inherit Grails' default dependencies
  inherits("global") {
    // specify dependency exclusions here; for example, uncomment this to disable ehcache:
    // excludes 'ehcache'
  }
  log "error" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
  checksums true // Whether to verify checksums on resolve
  legacyResolve false // whether to do a secondary resolve on plugin installation, not advised and here for backwards compatibility

  repositories {
    inherits true // Whether to inherit repository definitions from plugins

    grailsPlugins()
    grailsHome()
    mavenLocal()
    grailsCentral()
    mavenCentral()
    // uncomment these (or add new ones) to enable remote dependency resolution from public Maven repositories
    //mavenRepo "http://repository.codehaus.org"
    //mavenRepo "http://download.java.net/maven/2/"
    //mavenRepo "http://repository.jboss.com/maven2/"
    mavenRepo "http://repo.spring.io/milestone/"
    mavenRepo "http://download.java.net/maven/2/"
  }

  dependencies {

    // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes e.g.
    compile 'com.google.http-client:google-http-client-jackson2:1.17.0-rc'
    compile 'com.google.apis:google-api-services-analytics:v3-rev83-1.17.0-rc'
    runtime 'mysql:mysql-connector-java:5.1.24'
    runtime 'c3p0:c3p0:0.9.1.2'

  }

  plugins {
    // plugins for the build system only
    build ":tomcat:7.0.42"

    // plugins for the compile step
    compile ":mail:1.0.1"
    compile ":email-confirmation:2.0.8"
    compile ':cache:1.1.1'
    compile ":spring-security-core:2.0-RC2"
    compile ":spring-security-facebook:0.15.2-CORE2"
	compile ":spring-security-oauth:2.0.2"
    compile ":spring-security-oauth-google:0.2"
	compile ":json-apis:0.9"
    // plugins needed at runtime but not for compilation
    runtime ":hibernate:3.6.10.2" // or ":hibernate4:4.1.11.2"
    runtime ":database-migration:1.3.5"
    runtime ":resources:1.2.7"
    // Uncomment these (or add new ones) to enable additional resources capabilities
    //runtime ":zipped-resources:1.0.1"
    //runtime ":cached-resources:1.1"
    //runtime ":yui-minify-resources:0.1.5"
  }
}
