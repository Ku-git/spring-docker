package com.licence.service;

import com.licence.model.License;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


class LicenseServiceTest {

    @Autowired
    private LicenseService licenseService;


    @Test
    void findLicensesByOrganizationId() {
//        ExecutorService executorService = Executors.newFixedThreadPool(6);
//        List<Callable<Void>> callables = new ArrayList<>();
//
//        // 使用通用的方法來創建Callable任務
//        callables.add(createTask("ec773d30-75e6-437d-a1ea-f546a5ae1a35", "Task 1"));
//        callables.add(createTask("ec773d30-75e6-437d-a1ea-f546a5ae1a35", "Task 2"));
//        callables.add(createTask("ec773d30-75e6-437d-a1ea-f546a5ae1a35", "Task 3"));
//        callables.add(createTask("ec773d30-75e6-437d-a1ea-f546a5ae1a35", "Task 4"));
//
//        try {
//            List<Future<Void>> results = executorService.invokeAll(callables);
//            for (Future<Void> result : results) {
//                try {
//                    result.get(); // 檢查每個 Callable 的執行結果
//                } catch (ExecutionException e) {
//                    System.err.println("ExecutionException: " + e.getCause().getMessage());
//                }
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            System.err.println("Test interrupted: " + e.getMessage());
//        } finally {
//            executorService.shutdown();
//        }
    }

    // 通用的Callable創建方法
    Callable<Void> createTask(String orgId, String taskName) {
        return () -> {
            try {
                licenseService.findLicensesByOrganizationId(orgId);
            } catch (TimeoutException e) {
                System.err.println(taskName + " failed: " + e.getMessage());
                throw new RuntimeException(e);
            }
            return null;
        };
    }

    @Test
    public void testBulkhead() throws InterruptedException {

//        String url = "http://127.0.0.1:8080/v1/organization/ec773d30-75e6-437d-a1ea-f546a5ae1a35/license";
//        Map<Integer, Integer> responseStatusCount = new ConcurrentHashMap<>();
//        CountDownLatch latch = new CountDownLatch(5);
//        // 定義並發請求數量
//        int numberOfRequests = 5;
//        RestTemplate restTemplate = new RestTemplate();
//
//        // 創建線程池以進行並發測試
//        ExecutorService executorService = Executors.newFixedThreadPool(numberOfRequests);
//
//        IntStream.rangeClosed(1, 5)
//                .forEach(i -> executorService.execute(() -> {
//                    ResponseEntity response = restTemplate.getForEntity(url, String.class);
//                    System.out.println(response.getBody());
//                    int statusCode = response.getStatusCodeValue();
//                    responseStatusCount.merge(statusCode, 1, Integer::sum);
//                    latch.countDown();
//                }));
//        latch.await();
//        executorService.shutdown();
//
//        assertEquals(2, responseStatusCount.keySet().size());
    }

}