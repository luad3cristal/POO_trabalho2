# Sistema de Biblioteca - Arquitetura Microkernel

Sistema de gerenciamento de biblioteca desenvolvido com arquitetura microkernel em Java 24, utilizando JavaFX para interface gráfica e MariaDB como banco de dados.

## 🏗️ Arquitetura

- **Core**: Aplicação principal com funcionalidades básicas
- **Plugins**: Módulos independentes (User, Book, Loan, Report)
- **Interfaces**: Contratos para comunicação entre componentes
- **Database**: MariaDB executado via Docker

## 🚀 Build e Execução

### Pré-requisitos

- Java 24
- Maven 3.9+
- Docker (para banco de dados)

### Build do Projeto

```bash
# Compilar todos os módulos
mvn clean install

# Compilar apenas um módulo específico
mvn -f interfaces/pom.xml install
mvn -f app/pom.xml install
mvn -f plugins/user-plugin/pom.xml install
```

### Executar Aplicação

```bash
# Executar aplicação principal
mvn exec:java -pl app

# Ou executar a partir do diretório app
cd app
mvn exec:java
```

### Banco de Dados

```bash
# Iniciar banco via Docker
docker-compose up -d

# Parar banco
docker-compose down

# Ver logs do banco
docker-compose logs mariadb
```

## 🧪 Testes

### Testes de Integração dos Plugins

```bash
# Executar todos os testes de plugins
mvn -f app/pom.xml test -Dtest="PluginTests"

# Testa:
# - Funcionamento individual de cada plugin
# - Recarregamento dinâmico de plugins
# - Isolamento entre plugins
# - Comunicação apenas via interfaces
# - UI modular e dinâmica
```

### Testes Funcionais do Sistema

```bash
# Executar testes funcionais
mvn -f app/pom.xml test -Dtest="SystemFunctionalTest"

# Testa:
# - Estrutura de CRUDs implementada
# - Navegação e UI
# - Relatórios
# - Testes unitários (estrutura)
# - Integração banco → serviço → UI
# - Sistema sem plugins específicos
# - Graceful degradation
```

### Executar Todos os Testes

```bash
# Compilar testes sem executar
mvn -f app/pom.xml test-compile

#Executar os testes do app
mvn -f app/pom.xml test

# Ou execute cada classe separadamente:
mvn -f app/pom.xml test -Dtest="PluginTest"
mvn -f app/pom.xml test -Dtest="SystemFunctionalTest"

# Ver relatórios de teste
# Verificar: app/target/surefire-reports/
```

## 🔌 Criação de Novos Plugins

### 1. Estrutura do Plugin

```bash
# Criar pasta do plugin
mkdir plugins/meu-plugin
cd plugins/meu-plugin
```

### 2. Configurar pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>br.edu.ifba.inf008</groupId>
        <artifactId>microkernel</artifactId>
        <version>1.0-SNAPSHOT</version>
        <relativePath>../../pom.xml</relativePath>
    </parent>

    <artifactId>meu-plugin</artifactId>
    <packaging>jar</packaging>

    <dependencies>
        <dependency>
            <groupId>br.edu.ifba.inf008</groupId>
            <artifactId>interfaces</artifactId>
            <version>1.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</project>
```

### 3. Implementar Plugin

```java
package br.edu.ifba.inf008.plugins;

import br.edu.ifba.inf008.interfaces.IPlugin;
import br.edu.ifba.inf008.interfaces.ICore;

public class MeuPlugin implements IPlugin {
    @Override
    public boolean init() {
        // Implementar inicialização do plugin
        // Registrar UI, services, etc.
        return true;
    }
}
```

### 4. Adicionar ao Build Principal

Editar `pom.xml` raiz:

```xml
<modules>
    <module>interfaces</module>
    <module>app</module>
    <module>plugins/user-plugin</module>
    <module>plugins/book-plugin</module>
    <module>plugins/loan-plugin</module>
    <module>plugins/report-plugin</module>
    <module>plugins/meu-plugin</module> <!-- ADICIONAR AQUI -->
</modules>
```

### 5. Build e Deploy

```bash
# Build do novo plugin
mvn -f plugins/meu-plugin/pom.xml install

# Copiar JAR para pasta plugins
cp plugins/meu-plugin/target/meu-plugin-1.0-SNAPSHOT.jar plugins/MeuPlugin.jar

# Rebuild completo
mvn clean install
```

## 📁 Estrutura do Projeto

```
microkernel/
├── pom.xml                     # POM principal
├── docker-compose.yml          # Configuração do banco
├── README.md                   # Este arquivo
├── app/                        # Aplicação principal
│   ├── pom.xml
│   ├── src/main/java/          # Core da aplicação
│   └── src/test/java/          # Testes
├── interfaces/                 # Contratos e interfaces
│   ├── pom.xml
│   └── src/main/java/
├── plugins/                    # Plugins do sistema
│   ├── *.jar                   # JARs compilados
│   ├── user-plugin/            # Plugin de usuários
│   ├── book-plugin/            # Plugin de livros
│   ├── loan-plugin/            # Plugin de empréstimos
│   └── report-plugin/          # Plugin de relatórios
└── docker/                     # Configurações do banco
    ├── init.sql
    └── README.md
```

## 🔧 Solução de Problemas

### Erro de Compilação

```bash
# Limpar e recompilar
mvn clean
mvn install

# Verificar versão do Java
java -version
mvn -version
```

### Banco de Dados

```bash
# Verificar se Docker está rodando
docker ps

# Recriar banco
docker-compose down
docker-compose up -d

# Conectar no banco manualmente
docker exec -it microkernel-mariadb-1 mysql -u library_user -p library_db
```

### Plugins Não Carregam

```bash
# Verificar JARs na pasta plugins
ls -la plugins/*.jar

# Recompilar plugins
mvn clean install
```

## 📊 Status dos Requisitos

✅ **Itens 29-33**: Testes de integração e isolamento dos plugins (PluginTests - 5 testes)  
✅ **Itens 34-37**: Testes funcionais finais do sistema (SystemFunctionalTest - 9 testes)  
✅ **Total**: 14 testes cobrindo todos os requisitos da TO-DO list  
✅ **Arquitetura Microkernel**: Implementada com core + plugins  
✅ **UI Dinâmica**: JavaFX com carregamento modular  
✅ **Banco de Dados**: MariaDB com Docker  
✅ **Build Automatizado**: Maven multi-módulo

## 👥 Equipe de Desenvolvimento

Projeto desenvolvido para a disciplina INF008 - Arquitetura de Software  
IFBA - Instituto Federal da Bahia
