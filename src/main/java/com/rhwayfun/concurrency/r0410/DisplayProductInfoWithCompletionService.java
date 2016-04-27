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
public class DisplayProductInfoWithCompletionService {

    //线程池
    private final ExecutorService executorService;
    //日期格式器
    private final DateFormat format = new SimpleDateFormat("HH:mm:ss");

    public DisplayProductInfoWithCompletionService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void renderProductDetail() {

        final List<ProductInfo> productInfos = loadProductInfos();

        CompletionService<ProductImage> completionService = new ExecutorCompletionService<ProductImage>(executorService);

        //为每个图像的下载建立一个工作任务
        for (final ProductInfo info : productInfos) {
            completionService.submit(new Callable<ProductImage>() {
                @Override
                public ProductImage call() throws Exception {
                    return info.getImage();
                }
            });
        }

        //展示商品简介的信息
        renderProductText(productInfos);

        try {
            //显示商品图片
            for (int i = 0, n = productInfos.size(); i < n; i++){
                Future<ProductImage> imageFuture = completionService.take();
                ProductImage image = imageFuture.get();
                renderProductImage(image);
            }
        } catch (InterruptedException e) {
            // 如果显示图片发生中断异常则重新设置线程的中断状态
            // 这样做可以让wait中的线程唤醒
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            try {
                throw new Throwable(e.getCause());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }

    /**
     * 带超时限制的加载方法
     * @param time
     * @param unit
     */
    public void renderProductDetail(long time,TimeUnit unit) {
        final List<ProductInfo> productInfos = loadProductInfos();

        CompletionService<ProductImage> completionService = new ExecutorCompletionService<ProductImage>(executorService);

        //为每个图像的下载建立一个工作任务
        for (final ProductInfo info : productInfos) {
            completionService.submit(new Callable<ProductImage>() {
                @Override
                public ProductImage call() throws Exception {
                    return info.getImage();
                }
            });
        }

        //展示商品简介的信息
        renderProductText(productInfos);

        try {
            //显示商品图片
            for (int i = 0, n = productInfos.size(); i < n; i++){
                Future<ProductImage> imageFuture = completionService.poll(time,unit);
                ProductImage image = imageFuture.get();
                renderProductImage(image);
            }
        } catch (InterruptedException e) {
            // 如果显示图片发生中断异常则重新设置线程的中断状态
            // 这样做可以让wait中的线程唤醒
            Thread.currentThread().interrupt();
        } catch (ExecutionException e) {
            try {
                throw new Throwable(e.getCause());
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }

    }

    private void renderProductImage(ProductImage image) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " display products images! "
                + format.format(new Date()));
    }

    private void renderProductText(List<ProductInfo> productInfos) {
        for (ProductInfo info : productInfos) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(Thread.currentThread().getName() + " display products description! "
                + format.format(new Date()));
    }

    private List<ProductInfo> loadProductInfos() {
        List<ProductInfo> list = new ArrayList<>();
        try {
            TimeUnit.SECONDS.sleep(3);
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
    private static class ProductInfo {
        private ProductImage image;

        public ProductImage getImage() {
            return image;
        }

        public void setImage(ProductImage image) {
            this.image = image;
        }
    }

    private static class ProductImage {
    }

    public static void main(String[] args) {
        DisplayProductInfoWithCompletionService cd = new DisplayProductInfoWithCompletionService(Executors.newCachedThreadPool());
        cd.renderProductDetail();
    }
}
