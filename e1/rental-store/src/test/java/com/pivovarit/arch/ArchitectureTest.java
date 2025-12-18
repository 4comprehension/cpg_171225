package com.pivovarit.arch;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noFields;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

@AnalyzeClasses(packages = "com.pivovarit", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule noFieldInjection = noFields()
        .should().beAnnotatedWith(Autowired.class);

    @ArchTest
    static final ArchRule shouldBeFreeOfCycles = slices()
      .matching("com.pivovarit.(*)..")
      .should().beFreeOfCycles()
      .as("high-level packages should be free of cycles")
      .because("cycles are bad");

    @ArchTest
    static final ArchRule noDefaultExecutorInCompletableFuture = ArchRuleDefinition.noClasses()
      .should().callMethod(CompletableFuture.class, "supplyAsync", Supplier.class)
      .orShould().callMethod(CompletableFuture.class, "runAsync", Runnable.class)
      .orShould().callMethod(CompletableFuture.class, "thenRunAsync", Runnable.class)
      .orShould().callMethod(CompletableFuture.class, "thenAcceptAsync", Consumer.class)
      .orShould().callMethod(CompletableFuture.class, "thenApplyAsync", Function.class)
      .orShould().callMethod(CompletableFuture.class, "thenCombineAsync", CompletionStage.class, BiFunction.class)
      .orShould().callMethod(CompletableFuture.class, "runAfterBothAsync", CompletionStage.class, Runnable.class)
      .orShould().callMethod(CompletableFuture.class, "thenAcceptBothAsync", CompletionStage.class, Consumer.class)
      .orShould().callMethod(CompletableFuture.class, "thenComposeAsync", CompletionStage.class)
      .orShould().callMethod(CompletableFuture.class, "completeAsync", Supplier.class)
      .orShould().callMethod(CompletableFuture.class, "exceptionallyComposeAsync", Function.class)
      .orShould().callMethod(CompletableFuture.class, "handleAsync", BiFunction.class)
      .orShould().callMethod(CompletableFuture.class, "applyToEitherAsync", CompletionStage.class, Function.class)
      .orShould().callMethod(CompletableFuture.class, "whenCompleteAsync", BiConsumer.class)
      .because("those default to ForkJoinPool.commonPool() which is a terrible default for most cases - consider Executors#newVirtualThreadPerTaskExecutor instead. If you really need ForkJoinPool, provide it explicitly (ForkJoinPool.commonPool())");
}
