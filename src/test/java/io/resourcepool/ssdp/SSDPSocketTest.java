package io.resourcepool.ssdp;

import io.resourcepool.ssdp.client.SsdpClient;
import io.resourcepool.ssdp.model.*;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Testing SSDP Socket receive
 *
 * @author Loïc Ortola on 08/01/2018
 */
public class SSDPSocketTest {

  /**
   * Warning: this test only verifies there is no failure during a SSDP check.
   * It does not check whether SSDP discovery actually works
   * @throws InterruptedException
   */
  @Test
  public void testSSDPReceive() throws InterruptedException {
    final CountDownLatch lock = new CountDownLatch(1);
    
    SsdpClient client = SsdpClient.create();
    DiscoveryRequest all = SsdpRequest.discoverAll();
    client.discoverServices(all, new DiscoveryListener() {
      @Override
      public void onServiceDiscovered(SsdpService service) {
        System.out.println("Found service: " + service);
      }

      @Override
      public void onServiceAnnouncement(SsdpServiceAnnouncement announcement) {
        System.out.println("Service announced something: " + announcement);
      }

      @Override
      public void onFailed(Exception ex) {
        System.err.println("Service failed to announce something: " + ex);
        throw new IllegalStateException(ex);
      }
    });
    
    lock.await(10, TimeUnit.SECONDS);
  }

  /**
   * Warning: this test only verifies there is no failure during a SSDP check.
   * It does not check whether SSDP discovery actually works.
   * @throws InterruptedException
   */
  @Test
  public void testSSDPReceiveIgnoreErrors() throws InterruptedException {
    final CountDownLatch lock = new CountDownLatch(1);

    SsdpClient client = SsdpClient.create();
    DiscoveryRequest all = SsdpRequest.discoverAll();
    client.discoverServices(all,
            SsdpClientOptions.builder().ignoreInterfaceDiscoveryErrors().build(),
            new DiscoveryListener() {
      @Override
      public void onServiceDiscovered(SsdpService service) {
        System.out.println("Found service: " + service);
      }

      @Override
      public void onServiceAnnouncement(SsdpServiceAnnouncement announcement) {
        System.out.println("Service announced something: " + announcement);
      }

      @Override
      public void onFailed(Exception ex) {
        System.err.println("Service failed to announce something: " + ex);
        throw new IllegalStateException(ex);
      }
    });

    lock.await(10, TimeUnit.SECONDS);
  }

}
