# Etapa 1: Base para ejecución (Runtime)
FROM mcr.microsoft.com/dotnet/aspnet:8.0 AS base
WORKDIR /app
EXPOSE 8080
ENV ASPNETCORE_URLS=http://+:8080
# Etapa 2: Construcción (SDK)
FROM mcr.microsoft.com/dotnet/sdk:8.0 AS build
WORKDIR /src

COPY ["Simex/Simex.sln", "Simex/"]
COPY ["Simex/Simex/Simex.csproj", "Simex/Simex/"]

RUN dotnet restore "Simex/Simex/Simex.csproj"

COPY . .

# Etapa 3: Publicación
WORKDIR /src/Simex/Simex
RUN dotnet publish "Simex.csproj" -c Release -o /app/publish /p:UseAppHost=false

# Etapa 4: Imagen final
FROM base AS final
WORKDIR /app
COPY --from=build /app/publish .
ENTRYPOINT ["dotnet", "Simex.dll"]
