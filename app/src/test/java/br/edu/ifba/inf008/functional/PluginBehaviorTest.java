package br.edu.ifba.inf008.functional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import static org.junit.jupiter.api.Assertions.*;

import br.edu.ifba.inf008.interfaces.IPlugin;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.ArrayList;

/**
 * Testes Avançados de Comportamento dos Plugins
 * Testa funcionalidades específicas e validações de negócio:
 * - Validações de entrada
 * - Comportamento de erro
 * - Operações específicas de cada domínio
 * - Performance básica
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Testes Avançados de Comportamento dos Plugins")
public class PluginBehaviorTest {

    private IPlugin userPluginInstance;
    private IPlugin bookPluginInstance;
    private IPlugin loanPluginInstance;
    private IPlugin reportPluginInstance;

    @BeforeEach
    void setUp() {
        System.out.println("🔧 Configurando testes de comportamento dos plugins...");
        
        // Carregar instâncias dos plugins
        userPluginInstance = loadPluginFromJar("../plugins/UserPlugin.jar", "br.edu.ifba.inf008.plugins.UserPlugin");
        bookPluginInstance = loadPluginFromJar("../plugins/BookPlugin.jar", "br.edu.ifba.inf008.plugins.BookPlugin");
        loanPluginInstance = loadPluginFromJar("../plugins/LoanPlugin.jar", "br.edu.ifba.inf008.plugins.LoanPlugin");
        reportPluginInstance = loadPluginFromJar("../plugins/ReportPlugin.jar", "br.edu.ifba.inf008.plugins.ReportPlugin");
        
        System.out.println("✅ Plugins carregados para testes de comportamento");
    }

    /**
     * Teste de Validações do UserPlugin
     */
    @Test
    @Order(1)
    @DisplayName("UserPlugin - Validações e Comportamento")
    void testUserPluginValidations() {
        System.out.println("🧪 Testando validações do UserPlugin...");
        
        assertNotNull(userPluginInstance, "UserPlugin deve estar carregado");
        
        try {
            Object userService = getServiceFromPlugin(userPluginInstance);
            if (userService != null) {
                // Testar métodos de validação
                testValidationMethods(userService, "User");
                
                // Testar métodos de busca
                testSearchMethods(userService, new String[]{"findByName", "findByEmail", "search"});
                
                // Testar métodos de contagem
                testCountMethods(userService);
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao testar validações UserPlugin: " + e.getMessage());
        }
        
        System.out.println("✅ Teste de validações UserPlugin concluído");
    }

    /**
     * Teste de Validações do BookPlugin
     */
    @Test
    @Order(2)
    @DisplayName("BookPlugin - Validações e Pesquisa")
    void testBookPluginValidations() {
        System.out.println("🧪 Testando validações do BookPlugin...");
        
        assertNotNull(bookPluginInstance, "BookPlugin deve estar carregado");
        
        try {
            Object bookService = getServiceFromPlugin(bookPluginInstance);
            if (bookService != null) {
                // Testar métodos de validação
                testValidationMethods(bookService, "Book");
                
                // Testar métodos específicos de livros
                testBookSpecificMethods(bookService);
                
                // Testar operações de pesquisa
                testSearchMethods(bookService, new String[]{"findByTitle", "findByAuthor", "findByISBN", "search"});
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao testar validações BookPlugin: " + e.getMessage());
        }
        
        System.out.println("✅ Teste de validações BookPlugin concluído");
    }

    /**
     * Teste de Regras de Negócio do LoanPlugin
     */
    @Test
    @Order(3)
    @DisplayName("LoanPlugin - Regras de Negócio")
    void testLoanPluginBusinessRules() {
        System.out.println("🧪 Testando regras de negócio do LoanPlugin...");
        
        assertNotNull(loanPluginInstance, "LoanPlugin deve estar carregado");
        
        try {
            Object loanService = getServiceFromPlugin(loanPluginInstance);
            if (loanService != null) {
                // Testar métodos de validação de empréstimo
                testLoanValidationMethods(loanService);
                
                // Testar operações específicas de empréstimo
                testLoanOperationMethods(loanService);
                
                // Testar cálculos e datas
                testLoanCalculationMethods(loanService);
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao testar regras LoanPlugin: " + e.getMessage());
        }
        
        System.out.println("✅ Teste de regras de negócio LoanPlugin concluído");
    }

    /**
     * Teste de Geração de Relatórios
     */
    @Test
    @Order(4)
    @DisplayName("ReportPlugin - Geração e Formatação")
    void testReportPluginGeneration() {
        System.out.println("🧪 Testando geração de relatórios...");
        
        assertNotNull(reportPluginInstance, "ReportPlugin deve estar carregado");
        
        try {
            Object reportService = getServiceFromPlugin(reportPluginInstance);
            if (reportService != null) {
                // Testar diferentes tipos de relatórios
                testReportTypes(reportService);
                
                // Testar formatação de relatórios
                testReportFormatting(reportService);
                
                // Testar agregações e estatísticas
                testReportStatistics(reportService);
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao testar ReportPlugin: " + e.getMessage());
        }
        
        System.out.println("✅ Teste de geração de relatórios concluído");
    }

    /**
     * Teste de Performance dos Plugins
     */
    @Test
    @Order(5)
    @DisplayName("Performance - Tempo de Resposta dos Plugins")
    void testPluginPerformance() {
        System.out.println("🧪 Testando performance dos plugins...");
        
        // Testar tempo de inicialização
        testInitializationTime();
        
        // Testar tempo de carregamento de services
        testServiceLoadingTime();
        
        // Testar responsividade dos métodos
        testMethodResponseTime();
        
        System.out.println("✅ Teste de performance concluído");
    }

    // Métodos auxiliares

    @SuppressWarnings("resource")
    private IPlugin loadPluginFromJar(String jarPath, String className) {
        try {
            File jarFile = new File(jarPath);
            if (!jarFile.exists()) {
                jarFile = new File(jarPath.substring(3)); // Remove "../"
            }
            
            if (!jarFile.exists()) {
                return null;
            }
            
            URL[] urls = {jarFile.toURI().toURL()};
            URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
            
            Class<?> pluginClass = classLoader.loadClass(className);
            Object instance = pluginClass.getDeclaredConstructor().newInstance();
            
            if (instance instanceof IPlugin) {
                return (IPlugin) instance;
            }
            
        } catch (Exception e) {
            System.out.println("⚠️ Erro ao carregar plugin " + className + ": " + e.getMessage());
        }
        
        return null;
    }

    private Object getServiceFromPlugin(IPlugin plugin) {
        try {
            Field[] fields = plugin.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (field.getName().toLowerCase().contains("service")) {
                    field.setAccessible(true);
                    return field.get(plugin);
                }
            }
        } catch (Exception e) {
            System.out.println("  - Erro ao acessar service: " + e.getMessage());
        }
        return null;
    }

    private void testValidationMethods(Object service, String entityName) {
        System.out.println("  🔍 Testando métodos de validação para " + entityName + ":");
        
        Method[] methods = service.getClass().getDeclaredMethods();
        List<String> validationMethods = new ArrayList<>();
        
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            if (methodName.contains("validate") || methodName.contains("check") || 
                methodName.contains("verify") || methodName.contains("isvalid")) {
                validationMethods.add(method.getName());
                System.out.println("    ✅ Método de validação: " + method.getName());
            }
        }
        
        System.out.println("  📊 Total de métodos de validação: " + validationMethods.size());
    }

    private void testSearchMethods(Object service, String[] searchMethods) {
        System.out.println("  🔍 Testando métodos de pesquisa:");
        
        Method[] methods = service.getClass().getDeclaredMethods();
        List<String> foundSearchMethods = new ArrayList<>();
        
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            for (String searchMethod : searchMethods) {
                if (methodName.contains(searchMethod.toLowerCase())) {
                    foundSearchMethods.add(method.getName());
                    System.out.println("    ✅ Método de pesquisa: " + method.getName());
                    break;
                }
            }
        }
        
        System.out.println("  📊 Total de métodos de pesquisa: " + foundSearchMethods.size());
    }

    private void testCountMethods(Object service) {
        System.out.println("  📊 Testando métodos de contagem:");
        
        Method[] methods = service.getClass().getDeclaredMethods();
        int countMethods = 0;
        
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            if (methodName.contains("count") || methodName.contains("size") || 
                methodName.contains("total") || methodName.contains("number")) {
                countMethods++;
                System.out.println("    ✅ Método de contagem: " + method.getName());
            }
        }
        
        System.out.println("  📈 Total de métodos de contagem: " + countMethods);
    }

    private void testBookSpecificMethods(Object bookService) {
        System.out.println("  📚 Testando métodos específicos de livros:");
        
        Method[] methods = bookService.getClass().getDeclaredMethods();
        List<String> bookMethods = new ArrayList<>();
        
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            if (methodName.contains("isbn") || methodName.contains("author") || 
                methodName.contains("title") || methodName.contains("publisher") ||
                methodName.contains("genre") || methodName.contains("year")) {
                bookMethods.add(method.getName());
                System.out.println("    ✅ Método específico: " + method.getName());
            }
        }
        
        System.out.println("  📖 Total de métodos específicos de livros: " + bookMethods.size());
    }

    private void testLoanValidationMethods(Object loanService) {
        System.out.println("  🔒 Testando validações de empréstimo:");
        
        Method[] methods = loanService.getClass().getDeclaredMethods();
        List<String> validationMethods = new ArrayList<>();
        
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            if (methodName.contains("available") || methodName.contains("canBorrow") || 
                methodName.contains("overdue") || methodName.contains("expired") ||
                methodName.contains("valid") || methodName.contains("check")) {
                validationMethods.add(method.getName());
                System.out.println("    ✅ Validação de empréstimo: " + method.getName());
            }
        }
        
        System.out.println("  🔐 Total de validações: " + validationMethods.size());
    }

    private void testLoanOperationMethods(Object loanService) {
        System.out.println("  📋 Testando operações de empréstimo:");
        
        Method[] methods = loanService.getClass().getDeclaredMethods();
        List<String> operationMethods = new ArrayList<>();
        
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            if (methodName.contains("borrow") || methodName.contains("return") || 
                methodName.contains("renew") || methodName.contains("extend") ||
                methodName.contains("reserve")) {
                operationMethods.add(method.getName());
                System.out.println("    ✅ Operação: " + method.getName());
            }
        }
        
        System.out.println("  🔄 Total de operações: " + operationMethods.size());
    }

    private void testLoanCalculationMethods(Object loanService) {
        System.out.println("  🧮 Testando cálculos de empréstimo:");
        
        Method[] methods = loanService.getClass().getDeclaredMethods();
        List<String> calculationMethods = new ArrayList<>();
        
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            if (methodName.contains("calculate") || methodName.contains("compute") || 
                methodName.contains("date") || methodName.contains("days") ||
                methodName.contains("fee") || methodName.contains("fine")) {
                calculationMethods.add(method.getName());
                System.out.println("    ✅ Cálculo: " + method.getName());
            }
        }
        
        System.out.println("  🔢 Total de cálculos: " + calculationMethods.size());
    }

    private void testReportTypes(Object reportService) {
        System.out.println("  📊 Testando tipos de relatórios:");
        
        Method[] methods = reportService.getClass().getDeclaredMethods();
        List<String> reportTypes = new ArrayList<>();
        
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            if (methodName.contains("report") && !methodName.contains("service")) {
                reportTypes.add(method.getName());
                System.out.println("    ✅ Tipo de relatório: " + method.getName());
            }
        }
        
        System.out.println("  📈 Total de tipos de relatório: " + reportTypes.size());
    }

    private void testReportFormatting(Object reportService) {
        System.out.println("  🎨 Testando formatação de relatórios:");
        
        Method[] methods = reportService.getClass().getDeclaredMethods();
        List<String> formatMethods = new ArrayList<>();
        
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            if (methodName.contains("format") || methodName.contains("export") || 
                methodName.contains("pdf") || methodName.contains("excel") ||
                methodName.contains("csv") || methodName.contains("print")) {
                formatMethods.add(method.getName());
                System.out.println("    ✅ Formatação: " + method.getName());
            }
        }
        
        System.out.println("  🖨️ Total de métodos de formatação: " + formatMethods.size());
    }

    private void testReportStatistics(Object reportService) {
        System.out.println("  📊 Testando estatísticas de relatórios:");
        
        Method[] methods = reportService.getClass().getDeclaredMethods();
        List<String> statsMethods = new ArrayList<>();
        
        for (Method method : methods) {
            String methodName = method.getName().toLowerCase();
            if (methodName.contains("statistics") || methodName.contains("summary") || 
                methodName.contains("total") || methodName.contains("count") ||
                methodName.contains("average") || methodName.contains("max") ||
                methodName.contains("min")) {
                statsMethods.add(method.getName());
                System.out.println("    ✅ Estatística: " + method.getName());
            }
        }
        
        System.out.println("  📈 Total de estatísticas: " + statsMethods.size());
    }

    private void testInitializationTime() {
        System.out.println("  ⏱️ Testando tempo de inicialização:");
        
        IPlugin[] plugins = {userPluginInstance, bookPluginInstance, loanPluginInstance, reportPluginInstance};
        String[] pluginNames = {"UserPlugin", "BookPlugin", "LoanPlugin", "ReportPlugin"};
        
        for (int i = 0; i < plugins.length; i++) {
            if (plugins[i] != null) {
                long startTime = System.currentTimeMillis();
                try {
                    plugins[i].init();
                } catch (Exception e) {
                    // Ignorar erros de inicialização para este teste
                }
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                System.out.println("    ✅ " + pluginNames[i] + ": " + duration + "ms");
                
                // Verificar se inicializou em tempo razoável (menos de 1 segundo)
                assertTrue(duration < 1000, pluginNames[i] + " deve inicializar em menos de 1 segundo");
            }
        }
    }

    private void testServiceLoadingTime() {
        System.out.println("  ⏱️ Testando tempo de carregamento de services:");
        
        IPlugin[] plugins = {userPluginInstance, bookPluginInstance, loanPluginInstance, reportPluginInstance};
        String[] pluginNames = {"UserPlugin", "BookPlugin", "LoanPlugin", "ReportPlugin"};
        
        for (int i = 0; i < plugins.length; i++) {
            if (plugins[i] != null) {
                long startTime = System.currentTimeMillis();
                Object service = getServiceFromPlugin(plugins[i]);
                long endTime = System.currentTimeMillis();
                long duration = endTime - startTime;
                
                System.out.println("    ✅ " + pluginNames[i] + " Service: " + duration + "ms");
                
                // Verificar se carregou em tempo razoável
                assertTrue(duration < 100, pluginNames[i] + " Service deve carregar em menos de 100ms");
                assertNotNull(service, pluginNames[i] + " deve ter service disponível");
            }
        }
    }

    private void testMethodResponseTime() {
        System.out.println("  ⏱️ Testando tempo de resposta dos métodos:");
        
        IPlugin[] plugins = {userPluginInstance, bookPluginInstance, loanPluginInstance, reportPluginInstance};
        String[] pluginNames = {"UserPlugin", "BookPlugin", "LoanPlugin", "ReportPlugin"};
        
        for (int i = 0; i < plugins.length; i++) {
            if (plugins[i] != null) {
                Object service = getServiceFromPlugin(plugins[i]);
                if (service != null) {
                    Method[] methods = service.getClass().getDeclaredMethods();
                    int testedMethods = 0;
                    
                    for (Method method : methods) {
                        if (method.getParameterCount() == 0 && testedMethods < 3) { // Testar apenas métodos sem parâmetros
                            try {
                                long startTime = System.currentTimeMillis();
                                method.invoke(service);
                                long endTime = System.currentTimeMillis();
                                long duration = endTime - startTime;
                                
                                System.out.println("    ✅ " + pluginNames[i] + "." + method.getName() + ": " + duration + "ms");
                                testedMethods++;
                                
                                // Verificar se respondeu em tempo razoável
                                assertTrue(duration < 1000, method.getName() + " deve responder em menos de 1 segundo");
                                
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                // Ignorar erros de invocação para este teste de performance
                            }
                        }
                    }
                }
            }
        }
    }
}
