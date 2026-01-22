# Sistema de Filtros para Paginação de Usuários

## 📋 Visão Geral

Implementação moderna e performática de filtros dinâmicos para listagem de usuários usando **Spring Data JPA Specifications**.

## 🎯 Funcionalidades

### Filtros Disponíveis

1. **username** - Busca parcial (LIKE) case-insensitive
2. **userType** - Busca exata por tipo de usuário (USER, ADMIN, INSTRUCTOR)
3. **userStatus** - Busca exata por status (ACTIVE, BLOCKED)

## 🚀 Como Usar

### Exemplos de Requisições

#### 1. Buscar todos os usuários (sem filtros)
```http
GET /users?page=0&size=20
```

#### 2. Filtrar por username
```http
GET /users?username=yan&page=0&size=20
```

#### 3. Filtrar por userType
```http
GET /users?userType=ADMIN&page=0&size=20
```

#### 4. Filtrar por userStatus
```http
GET /users?userStatus=ACTIVE&page=0&size=20
```

#### 5. Combinar múltiplos filtros (AND)
```http
GET /users?username=yan&userType=USER&userStatus=ACTIVE&page=0&size=20
```

#### 6. Ordenação
```http
GET /users?username=test&sort=creationDate,desc&page=0&size=10
```

## 🏗️ Arquitetura

### Componentes Criados

1. **UserFilterDto** - Record para encapsular os filtros
2. **UserSpecifications** - Classe com lógica de construção das queries dinâmicas
3. **Atualização no UserService/UserServiceImpl** - Novo método com suporte a filtros
4. **Atualização no UserController** - Endpoint recebe query params

### Vantagens da Implementação

✅ **Performance**: Apenas filtros não-nulos são aplicados na query  
✅ **Type-Safe**: Uso de enums garante valores válidos  
✅ **Flexível**: Fácil adicionar novos filtros  
✅ **Clean Code**: Separação de responsabilidades  
✅ **Modern**: Uso de Records e Specifications  
✅ **Testável**: Lógica isolada em classes específicas  

## 📊 Resposta JSON

```json
{
  "content": [...],
  "page": {
    "size": 20,
    "number": 0,
    "totalElements": 5,
    "totalPages": 1
  }
}
```

## 🔧 Extensibilidade

Para adicionar novos filtros:

1. Adicione o campo no `UserFilterDto`
2. Adicione o predicado em `UserSpecifications.withFilters()`
3. Adicione o `@RequestParam` no controller

Exemplo:
```java
// 1. UserFilterDto
public record UserFilterDto(
    String username,
    UserType userType,
    UserStatus userStatus,
    String email  // novo filtro
) {}

// 2. UserSpecifications
if (filter.email() != null && !filter.email().isBlank()) {
    predicates.add(
        criteriaBuilder.like(
            criteriaBuilder.lower(root.get("email")),
            "%" + filter.email().toLowerCase() + "%"
        )
    );
}

// 3. UserController
@GetMapping
public ResponseEntity<PageDto<UserModel>> getAllUsers(
    @RequestParam(required = false) String username,
    @RequestParam(required = false) UserType userType,
    @RequestParam(required = false) UserStatus userStatus,
    @RequestParam(required = false) String email,  // novo param
    Pageable pageable) {
    
    UserFilterDto filter = new UserFilterDto(username, userType, userStatus, email);
    return ResponseEntity.ok(PageDto.from(userService.findAll(filter, pageable)));
}
```

## 🎨 Boas Práticas Implementadas

- ✅ Uso de Records para DTOs imutáveis
- ✅ Specifications para queries dinâmicas
- ✅ @Transactional(readOnly = true) para otimização
- ✅ Validação de valores nulos antes de aplicar filtros
- ✅ Case-insensitive para buscas de texto
- ✅ Documentação inline no código
- ✅ Separação de responsabilidades (Controller → Service → Repository)
