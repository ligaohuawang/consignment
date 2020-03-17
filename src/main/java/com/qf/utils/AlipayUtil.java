package com.qf.utils;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;

public class AlipayUtil {
    public static final String ALIPAY_PUBLICK_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApOlKFwSPrxRy9TMgaZuP8pwgWMpgmauxY7px5CXobrsrvCqVL7Wav53D1+vRQzsu4bboLa39uf/od8/Dsz4iTw6kwTOCsijJ/Ythiozo2PM+lTNh5pkz/D765WGiko1OYwTVxMgpzpqbkLwjeNU2ATOizlRkqlmiVek8yLYXEQQOTxjU+aEuIOQ6qOjiwaTMA7WdR3MLp20T1JwUqcLBf963er1nqCUPQTCAjnCMGjw3G2eI/+nVd9vfawX9VhDVmmM5pxHNt1PC/wlwqw3pGjyeVdUHz9PE60yC0ajWjFfMJDqRFaUVEGPpRim3+mN0mWukWWADr0KGslec4Hg9PwIDAQAB";

    private static AlipayClient alipayClient;

    static {
        alipayClient = new DefaultAlipayClient(
                //支付宝网关
                "https://openapi.alipaydev.com/gateway.do",
                //创建应用后生成
                "2016101500690008",
                //开发者私钥
                "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCKFFQ/nH357gQdmtyFBB/3jb1muvzE0GmS2jyAaMlvSgq46i6rVxAHsNQGoHduZBMHYAh1HmYlSgLyKJ4LFh4IYpRCRFtnmMJohQjTyIVqoM9AEl2ki/Ek+EaO5OKZ7y/igM92rjojwyHLQOyOWJ5EuddObHTjy3BwnBh2p5U9v6i4H7MDjnd9nBg7R7ZqOqfbVw62CjijWK5g7CbIeQHcWoJYTlgcbry1B9WEDouCrBmTr4sQFx9o5D8jnupULiX5F7KImXi9VydwPQTY2hO7GXpegaVcgOZtShqDgr1PdMh+44ZUJTEDSmY93fgEb+WkSA1L1YIsDRaMJNWFwHNDAgMBAAECggEAPJaBBUyQxptXOPpb6w4NTN3oDuzbvyzINoUGaT4RrYCqxcsUYWCZixZfYu0Emu7UnrbX3zRQk+ou8r055Bl3YiyHa7B8/y87lwtQ/UUOreQULMQiTPKPBK7gnxD2EZ+vvk7ROf1nS4vzBzHc2vUttXBqdmIGxHHfS7pizaXpSadrxk23pKY4NDIa83oyPEsV7xnYpUIKlu9k95Q0JsQDU9RC6PfBmoGj1zynVQJVgoVpLiPQC2XxXZPV0cKp6XJlnlU3l9zy310RxMezzhgNLWWjatLaBvr1zeKiBCY/rgweNRCc/IXL7RcE1QVIjep+7Utv6NYPsToBe1DgHLtrWQKBgQC/UWoO5wYg+yv3cneau3dow7a8rWXUMTBy/J0pH/BQHsD0563AJBlMxZeQvpEqz0PzXzKgJxzmwe3jffi+eNLzbT9mldyv9LCgxWYukTF6+MTmDJLren0ilvVCQ9WaaYuMeCIfODpnUWa/bPF3WpO57JXwufVoIoXHkaSseG/CVwKBgQC4wxwbCpP+ABbrApPulHHqAzKcuyR/AFfJzyRg5m3qbBYR0n3w9nrb4l2hnYNWa4pM+Bh7SKPvP8Q3pna8CRxWrHkNiw7NbY4UWM0p/3ID2SzOW6/Z8O0MbU76o8myL1jSL84WgeuVx0OONcsdQo8Gn8Ep0p8H7Xi4bk5QMIJ69QKBgQCIhKLmoWnZLQHRn8fDDRSHGGkUN7Na+AK3epT23IiR03AjEhME8X1wL+sYaTOTVbUKPBsW9vIDo8yJscFGwVbeR02feT6x/CvxfQqOHiMtuche1AgeqhgTD59ROrB4b+oZQTwwBOKACTEFVcr0PRj+6diQn1GDjmbh4oacX5B4HwKBgQCU+DOsFe5XKOlRmSV/gs8KOIPRXQ7DG5qsy8hwHLrY/UcaHZaP95v9uFl4GpEv18uN3aeXl20eVVK28A/h8pKXy75azhkkR0X8X9EkIKNkOPHofIKf/aKTl3rLmc3hTk2WQxcujJhPjneIrRTUxbqeQ6ayOvEgqjbgtYeNHl/GFQKBgQCQr5tmzK5YphbI0dM/cMcH2kqVj9QKynY+q8yCgRtikKAwtJzvpzX/Rcl8rNqTdrAB/P48LJMzvUbi41oQ3iQGkSnlzAzYQfuPV3gRe/Z6ulbgxUMeTkTrJlbbzCZx6warYqZQ8ne7iSj5tf3wE+Zi107Hv6xtlTVFuMlXm7yHYA==",
                //格式
                "json",
                //编码
                "UTF-8",
                //支付宝公钥
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApOlKFwSPrxRy9TMgaZuP8pwgWMpgmauxY7px5CXobrsrvCqVL7Wav53D1+vRQzsu4bboLa39uf/od8/Dsz4iTw6kwTOCsijJ/Ythiozo2PM+lTNh5pkz/D765WGiko1OYwTVxMgpzpqbkLwjeNU2ATOizlRkqlmiVek8yLYXEQQOTxjU+aEuIOQ6qOjiwaTMA7WdR3MLp20T1JwUqcLBf963er1nqCUPQTCAjnCMGjw3G2eI/+nVd9vfawX9VhDVmmM5pxHNt1PC/wlwqw3pGjyeVdUHz9PE60yC0ajWjFfMJDqRFaUVEGPpRim3+mN0mWukWWADr0KGslec4Hg9PwIDAQAB",
                "RSA2"); //获得初始化的AlipayClient
    }

    public static AlipayClient getAlipayClient() {
        return alipayClient;
    }
}
