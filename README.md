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
mvn exec:java -pl app
```

### 🚀 Quick Start

```bash
# 1. Clonar e entrar no diretório
git clone <repository-url>
cd microkernel

# 2. Compilar tudo
mvn clean install

# 3. Iniciar banco de dados (opcional)
docker-compose up -d

# 4. Executar todos os testes
mvn -f app/pom.xml test -Dtest="*Test*"

# 5. Executar aplicação
mvn exec:java -pl app
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

O sistema possui 24 testes automatizados distribuídos em 4 categorias que validam a arquitetura microkernel e funcionalidades dos plugins.

### � Comando Geral

```bash
# Executar TODOS os testes (recomendado)
mvn -f app/pom.xml test -Dtest="*Test*"
```

### 📋 Comandos Específicos

```bash
# Testes de Integração - Carregamento e isolamento de plugins
mvn -f app/pom.xml test -Dtest="PluginTest"

# Testes Funcionais - Estrutura do sistema e navegação
mvn -f app/pom.xml test -Dtest="SystemFunctionalTest"

# Testes CRUD - Operações Create, Read, Update, Delete
mvn -f app/pom.xml test -Dtest="PluginCRUDTest"

# Testes Comportamentais - Validações e performance
mvn -f app/pom.xml test -Dtest="PluginBehaviorTest"
```

### 🎯 O que os Comandos Realizam

| Comando                | Testes | Validação                                                      |
| ---------------------- | ------ | -------------------------------------------------------------- |
| `PluginTest`           | 5      | Carregamento dinâmico, isolamento e comunicação via interfaces |
| `SystemFunctionalTest` | 9      | Estrutura CRUD, navegação, banco de dados e build              |
| `PluginCRUDTest`       | 5      | Operações CRUD e integração entre plugins                      |
| `PluginBehaviorTest`   | 5      | Regras de negócio, performance e funcionalidades avançadas     |
| **Total**              | **24** | **Cobertura completa do sistema**                              |

**Resultado esperado:** `Tests run: 24, Failures: 0, Errors: 0, Skipped: 0`

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

### Problemas com Testes

#### Testes Não Executam

```bash
# Compilar testes primeiro
mvn -f app/pom.xml test-compile

# Executar com padrão específico
mvn -f app/pom.xml test -Dtest="*Test*"

# Verificar se classes de teste existem
ls -la app/src/test/java/br/edu/ifba/inf008/
```

#### Falhas de Plugin nos Testes

```bash
# Verificar se plugins estão compilados
ls -la plugins/*.jar

# Recompilar plugins antes dos testes
mvn clean install
mvn -f app/pom.xml test -Dtest="*Test*"
```

#### Aviso de Banco Indisponível

**Comportamento Normal:** Os testes mostram aviso quando MariaDB não está rodando, mas continuam executando testes de estrutura.

```bash
# Para executar com banco (opcional)
docker-compose up -d
mvn -f app/pom.xml test -Dtest="*Test*"
```

#### Verbose nos Testes

```bash
# Executar com mais detalhes
mvn -f app/pom.xml test -Dtest="*Test*" -X

# Ver apenas resultados dos testes
mvn -f app/pom.xml test -Dtest="*Test*" -q
```

## 🎯 Características Técnicas

- **Arquitetura**: Microkernel com carregamento dinâmico de plugins
- **Interface**: JavaFX com abas dinâmicas criadas pelos plugins
- **Banco de Dados**: MariaDB 11+ com Docker
- **Build**: Maven multi-módulo
- **Testes**: JUnit 5 com testes de integração e funcionais
- **Hot-reload**: Plugins podem ser recarregados sem reinicializar a aplicação

## 📋 Funcionalidades

### Gestão de Usuários

- Cadastro, edição e exclusão de usuários
- Listagem com pesquisa
- Validação de dados

### Gestão de Livros

- Controle de acervo
- Informações de autor, ISBN, ano
- Controle de disponibilidade

### Sistema de Empréstimos

- Registro de empréstimos e devoluções
- Controle de prazos
- Histórico de transações

### Relatórios

- Relatórios de empréstimos ativos
- Estatísticas de uso
- Exportação de dados

---

**Projeto desenvolvido para INF008 - Arquitetura de Software**  
**IFBA - Instituto Federal da Bahia**
