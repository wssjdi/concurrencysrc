package com.rhwayfun.concurrency.r0410;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by rhwayfun on 16-4-10.
 */
public class DisplayProductInfoWithExecutorService {

    //线程池
    private final ExecutorService executorService = Executors.newFixedThreadPool(2);
    //日期格式器
    private final DateFormat format = new SimpleDateFormat("HH:mm:ss");

    // 模拟电商网站商品详情的信息展示
    // 由于可能商品的图片可能会有很多张，所以显示商品的图片往往会有一定的延迟
    // 除了商品的详情外还包括商品简介等信息的展示，由于这里信息主要的是文字为
    // 主，所以能够比图片更快显示出来。下面的代码就以执行这两个任务为主线，完
    // 成这两个任务的执行。由于这两个任务的执行存在较大差距，所以想到的第一个
    // 思路就是异步执行，首先执行图像的下载任务，之后（不会很久）开始执行商品
    // 简介信息的展示，如果网络足够好，图片又不是很大的情况下，可能在开始展示
    // 商品的时候图像就下载完成了，所以自然想到使用Executor和Callable完成异
    // 步任务的执行。

    public void renderProductDetail() {
        final List<ProductInfo>  productInfos = loadProductImages();

        //异步下载图像的任务
        Callable<List<ProductImage>> task = new Callable<List<ProductImage>>() {

            @Override
            public List<ProductImage> call() throws Exception {
                List<ProductImage> imageList = new ArrayList<>();
                for (ProductInfo info : productInfos){
                    imageList.add(info.getImage());
                }
                return imageList;
            }
        };

        //提交给线程池执行
        Future<List<ProductImage>> listFuture = executorService.submit(task);
        //展示商品简介的信息
        renderProductText(productInfos);

        try {
            //显示商品的图片
            List<ProductImage> imageList = listFuture.get();
            renderProductImage(imageList);
        } catch (InterruptedException e) {
            // 如果显示图片发生中断异常则重新设置线程的中断状态
            // 这样做可以让wait中的线程唤醒
            Thread.currentThread().interrupt();
            // 同时取消任务的执行,参数false表示在线程在执行不中断
            listFuture.cancel(true);
        } catch (ExecutionException e) {
            try {
                throw new Throwable(e.getCause());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }

    private void renderProductImage(List<ProductImage> imageList ) {
        for (ProductImage image : imageList){
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " display products images! "
            + format.format(new Date()));
    }

    private void renderProductText(List<ProductInfo> productInfos) {
        for (ProductInfo info : productInfos){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " display products description! "
            + format.format(new Date()));
    }

    private List<ProductInfo> loadProductImages() {
        List<ProductInfo> list = new ArrayList<>();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProductInfo info = new ProductInfo();
        info.setImage(new ProductImage());
        list.add(info);
        System.out.println(Thread.currentThread().getName() + " load products info! "
                + format.format(new Date()));
        return list;
    }

    /**
     * 商品
     */
    private static class ProductInfo{
        private ProductImage image;

        public ProductImage getImage() {
            return image;
        }

        public void setImage(ProductImage image) {
            this.image = image;
        }
    }

    private static class ProductImage{}

    public static void main(String[] args){
        DisplayProductInfoWithExecutorService cd = new DisplayProductInfoWithExecutorService();
        cd.renderProductDetail();
        System.exit(0);
    }
}
