package pl.lejdi.gymdiary

import android.content.Context
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pl.lejdi.gymdiary.viewmodel.AddSetViewModel

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [Build.VERSION_CODES.P])
class AddSetViewModelTest {
    lateinit var instrumentationContext: Context

    @Before
    fun setup() {
        instrumentationContext = InstrumentationRegistry.getInstrumentation().context
    }

    @Test
    fun suggestedReps(){
        val addSetViewModel = AddSetViewModel()
        addSetViewModel.init(instrumentationContext)
        addSetViewModel.suggestedReps("Strength")
        Assert.assertTrue(addSetViewModel.suggestedReps.value == 5)
        addSetViewModel.suggestedReps("Hypertrophy")
        Assert.assertTrue(addSetViewModel.suggestedReps.value == 12)
        addSetViewModel.suggestedReps("Endurance")
        Assert.assertTrue(addSetViewModel.suggestedReps.value == 18)
        addSetViewModel.suggestedReps("qwerty")
        Assert.assertTrue(addSetViewModel.suggestedReps.value == 0)
        addSetViewModel.suggestedReps("")
        Assert.assertTrue(addSetViewModel.suggestedReps.value == 0)
    }

    @Test
    fun roundWeight(){
        val addSetViewModel = AddSetViewModel()
        addSetViewModel.init(instrumentationContext)
        val foo = addSetViewModel.javaClass.getDeclaredMethod("roundWeight", Float::class.java)
        foo.isAccessible = true
        Assert.assertTrue(foo.invoke(addSetViewModel, 10.0f) == 10.0f)
        Assert.assertTrue(foo.invoke(addSetViewModel, 10.3f) == 10.5f)
        Assert.assertTrue(foo.invoke(addSetViewModel, 10.9f) == 11.0f)
        Assert.assertTrue(foo.invoke(addSetViewModel, 0f) == 0.0f)
        Assert.assertTrue(foo.invoke(addSetViewModel, 10.05f) == 10.0f)
    }

    @Test
    fun calculateRM(){
        val addSetViewModel = AddSetViewModel()
        addSetViewModel.init(instrumentationContext)
        val foo = addSetViewModel.javaClass.getDeclaredMethod("calculateRM", Float::class.java, Int::class.java)
        foo.isAccessible = true
        Assert.assertTrue(foo.invoke(addSetViewModel, 0f, 100) == 0f)
        Assert.assertTrue(foo.invoke(addSetViewModel, 10f, 30) == 20.0f)
        Assert.assertTrue(foo.invoke(addSetViewModel, 50f, 120) == 250.0f)
    }
}